package View

import com.raquo.laminar.api.L.*

import Calendar.CalendarView

import Auth.LoginView


object MainViewController {
    /* View selected */
    enum View(val elem: Element) {
        case Calendar extends View(CalendarView())
        case Login extends View(LoginView())
    }

    // Current selected view
    private val currentView: Var[View] = Var(View.Calendar)

    // Get a signal of the current view
    val getCurrentView: Signal[View] = currentView.signal
    
    // Get the dom element for the current view
    val getCurrentViewElement: Signal[Element] = getCurrentView.map(_.elem)

    // Set the selected view
    val setCurrentView: Observer[View] = currentView.writer
}
