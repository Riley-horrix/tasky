import com.raquo.laminar.api.L.*

import org.scalajs.dom

import View.MainView

@main def app = {
    renderOnDomContentLoaded(
        dom.document.getElementById("app"),
        MainView()
    )
}
