package usecases.createwordle

import CreateWordleFailure
import arrow.core.left
import contexts.SaveWordleFailure
import core.Wordle
import createWordle
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.StringSpec
import usecases.WordleIds.id1
import usecases.Words.BEANS
import usecases.Words.CAKES

class CreateWordleTest : StringSpec({
    "Create a new wordle game" {
        with(DummyCreateWordleContext(id1, CAKES, mutableListOf())) {
            createWordle() shouldBeRight Wordle(
                id = id1,
                answer = CAKES,
            )
        }
    }

    "Can't create a new wordle game for an existing wordle ID" {
        // In retrospect, this is only needed if we think there will be duplicate UUIDs, which should basically never have in real life.
        // But whatever.
        with(
            DummyCreateWordleContext(
                generatedUUID = id1,
                dictionaryWord = CAKES,
                wordles = mutableListOf(
                    Wordle(
                        id = id1,
                        answer = BEANS,
                    )
                ),
            )
        ) {
            createWordle() shouldBeLeft CreateWordleFailure.WordleAlreadyExists(
                id = id1,
            )
        }
    }

    "Unknown failure when saving wordle" {
        with(
            DummyCreateWordleContext(
                generatedUUID = id1,
                dictionaryWord = CAKES,
                wordles = mutableListOf(),
                saveWordle = {
                    SaveWordleFailure.Unknown(
                        id = id1,
                        message = "Oops"
                    ).left()
                }
            )
        ) {
            createWordle() shouldBeLeft CreateWordleFailure.FailedToSaveWordle(
                id = id1,
                message = "Oops"
            )
        }
    }
})