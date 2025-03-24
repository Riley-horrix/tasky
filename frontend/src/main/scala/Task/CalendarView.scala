package Task

import com.raquo.laminar.api.L.*

val gridRow: StyleProp[Int] = styleProp("grid-row")

object CalendarView {
    def apply(): Element = div(
        className := "calendar-main-view",
        div(
            className := "calendar-day", "time", cls("line"),
            List.range(0, 16 * 2).map(index => 
                span(
                    className := "calendar-time-span",
                    s"${Time(6 + index / 2, (index * 30) % 60)}"
                )
            )
        ),
        div(
            className := "calendar-day", "mon", cls("line"),
            children <-- TaskManager.getMappedTaskList.map { tasks => 
                tasks.foldLeft(
                    (List(div(
                        gridRow := s"span ${
                            tasks.headOption.map( task =>
                                Time.minutesBetween(Time(6, 0), task.time) / 5
                            ).getOrElse(0)
                        }"
                    )), Nil)
                )(
                    (collector: (List[Element], List[StaticTask]), task: StaticTask) => (
                        TaskRenderer.renderCalendar(task) :: collector._1, task :: collector._2)
                )._1.reverse
            }
        ),
        div(className := "calendar-day", "tues", cls("line")),
        div(className := "calendar-day", "wed", cls("line")),
        div(className := "calendar-day", "thur", cls("line")),
        div(className := "calendar-day", "fri", cls("line")),
        div(className := "calendar-day", "sat", cls("line")),
        div(className := "calendar-day", "sun"),
    )
}
