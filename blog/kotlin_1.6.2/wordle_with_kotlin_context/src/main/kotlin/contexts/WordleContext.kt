package contexts

import CreateWordleContext
import usecases.GuessContext

// This is the full context to be able to run every usecase
interface WordleContext: CreateWordleContext, GuessContext
