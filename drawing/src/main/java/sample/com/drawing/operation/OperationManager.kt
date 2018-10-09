package sample.com.drawing.operation

/**
 * Created on 02.10.18.
 */
class OperationManager {
    private val executedOperations = mutableListOf<Operation>()
    private val undoneOperations = mutableListOf<Operation>()

    fun perform(operation: Operation) {
        operation.execute()
        executedOperations.add(operation)
    }

    fun undo(operation: (Operation) -> Unit) {
        val latestOperation = executedOperations.lastOrNull()
        if (latestOperation != null) {
            latestOperation.revert()
            undoneOperations.add(latestOperation)
            executedOperations.remove(latestOperation)
            operation.invoke(latestOperation)
        }
    }

    fun redo(operation: (Operation) -> Unit) {
        val latestOperation = undoneOperations.lastOrNull()
        if (latestOperation != null) {
            latestOperation.execute()
            executedOperations.add(latestOperation)
            undoneOperations.remove(latestOperation)
            operation.invoke(latestOperation)
        }
    }
}