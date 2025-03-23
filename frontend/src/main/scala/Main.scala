import com.raquo.laminar.api.L.*
import org.scalajs.dom

@main def app = {
    renderOnDomContentLoaded(
        dom.document.getElementById("app"),
        div()
    )
}
