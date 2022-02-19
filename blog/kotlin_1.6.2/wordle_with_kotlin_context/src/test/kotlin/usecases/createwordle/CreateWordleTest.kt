package usecases.createwordle

import CreateWordleFailure
import arrow.core.left
import contexts.SaveWordleFailure
import core.toWord
import createWordle
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.StringSpec
import usecases.wordles.Beans
import usecases.wordles.Cakes

class CreateWordleTest : StringSpec({
    "Create a new wordle game" {
        with(DummyCreateWordleContext(Cakes.wordle.id, "CAKES".toWord(), mutableListOf())) {
            createWordle() shouldBeRight Cakes.wordle
        }
    }

    "Can't create a new wordle game for an existing wordle ID" {
        // In retrospect, this is only needed if we think there will be duplicate UUIDs, which should basically never happen in real life.
        // But whatever...
        with(
            DummyCreateWordleContext(
                generatedUUID = Beans.wordle.id,
                dictionaryWord = "BEANS".toWord(),
                wordles = mutableListOf(
                    Beans.wordle
                ),
            )
        ) {
            createWordle() shouldBeLeft CreateWordleFailure.WordleAlreadyExists(
                id = Beans.wordle.id,
            )
        }
    }

    "Unknown failure when saving wordle" {
        with(
            DummyCreateWordleContext(
                generatedUUID = Beans.wordle.id,
                dictionaryWord = "CAKES".toWord(),
                wordles = mutableListOf(),
                saveWordle = {
                    SaveWordleFailure.Unknown(
                        id = Beans.wordle.id,
                        message = "Oops"
                    ).left()
                }
            )
        ) {
            createWordle() shouldBeLeft CreateWordleFailure.FailedToSaveWordle(
                id = Beans.wordle.id,
                message = "Oops"
            )
        }
    }
})