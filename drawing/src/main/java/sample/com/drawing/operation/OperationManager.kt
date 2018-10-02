package sample.com.drawing.operation

/**
 * Created on 02.10.18.
 */
class OperationManager {
    private val executedOperations = mutableListOf<Operation>()
    private val undoneOperations = mutableListOf<Operation>()

    fun apply(operation: Operation) {
        operation.execute()
        executedOperations.add(operation)
    }

    fun undo() {
        val latestOperation = executedOperations.lastOrNull()
        if (latestOperation != null) {
            latestOperation.revert()
            undoneOperations.add(latestOperation)
        }
    }

    fun redo() {
        val latestOperation = undoneOperations.lastOrNull()
        if (latestOperation != null) {
            latestOperation.execute()
            executedOperations.add(latestOperation)
        }
    }
}