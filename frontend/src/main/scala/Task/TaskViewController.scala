package Task

import com.raquo.laminar.api.L.*


object TaskViewController {
    enum TaskView(val elem: Element) {
        case Calendar extends TaskView(CalendarView())
    }

    // Current task view
    private val taskView: Var[TaskView] = Var(TaskView.Calendar)

    // Get a signal of the current view
    val getTaskView: Signal[TaskView] = taskView.signal
    
    // Get the dom element for the current view
    val getTaskViewElement: Signal[Element] = getTaskView.map(_.elem)

    // Set the selected view
    val setTaskView: Observer[TaskView] = taskView.writer
}
