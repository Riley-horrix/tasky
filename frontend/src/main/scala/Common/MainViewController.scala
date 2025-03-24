import com.raquo.laminar.api.L.*

import Task.TaskView

import Auth.AuthView

import Home.HomeView


object MainViewController {
    /* View selected */
    enum View(val elem: Element) {
        case Task extends View(TaskView())
        case Auth extends View(AuthView())
        case Home extends View(HomeView())
    }

    // Current selected view
    private val currentView: Var[View] = Var(View.Task)

    // Get a signal of the current view
    val getCurrentView: Signal[View] = currentView.signal
    
    // Get the dom element for the current view
    val getCurrentViewElement: Signal[Element] = getCurrentView.map(_.elem)

    // Set the selected view
    val setCurrentView: Observer[View] = currentView.writer
}
