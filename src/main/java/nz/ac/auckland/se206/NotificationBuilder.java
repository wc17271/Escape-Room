package nz.ac.auckland.se206;

import javafx.geometry.Pos;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

public class NotificationBuilder {

  public static Notifications createNotification(String message, int duration) {

    Notifications notification =
        Notifications.create()
            .title("Game Master:")
            .text(message)
            .position(Pos.BOTTOM_LEFT)
            .hideAfter(Duration.seconds(duration))
            .owner(App.getStage());

    return notification;
  }
}
