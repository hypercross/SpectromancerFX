package application;

import java.io.IOException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.swing.JOptionPane;

import network.NetworkHelper;
import provider.EntityAssembly;
import spectromancer.SpPlayer;
import spectromancer.playerClass.Necromancer;
import entity.EntityGame;
import game.PlayerClass;

public class DialogStartGame {

	private EntityGame game;
	private ActionHandler actionHandler;

	public DialogStartGame(ActionHandler ah)
	{
		this.actionHandler = ah;
		final Stage s = new Stage();
		VBox vbox = new VBox(8);

		Text t = new Text("Your class: ");
		final ChoiceBox<PlayerClass> cb = new ChoiceBox<PlayerClass>();
		cb.getItems().addAll(SpPlayer.vanilla);
		cb.setValue(Necromancer.instance);

		Text t2 = new Text("Opponent class: ");
		final ChoiceBox<PlayerClass> cb2 = new ChoiceBox<PlayerClass>();
		cb2.getItems().addAll(SpPlayer.vanilla);
		cb2.setValue(Necromancer.instance);

		final Button confirm = new Button("Start Game");
		confirm.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				confirm.setText("Starting...");
				confirm.setDisable(true);
				game = EntityAssembly.setupGameByClass(cb.getValue(), cb2.getValue());
				new Thread(new Runnable()
				{
					@Override
					public void run() {
						try {
							NetworkHelper.instance.startIntegrated(game,actionHandler);
							Platform.runLater(new Runnable()
							{
								public void run()
								{
									s.close();
								}
							});
						} catch (IOException e1) {
							e1.printStackTrace();
							JOptionPane.showMessageDialog(null, e1.toString() ,"Error!",  JOptionPane.PLAIN_MESSAGE);
						}
					}

				}).start();
			}
		});

		vbox.getChildren().addAll(t,cb, t2, cb2, confirm);

		s.setScene(new Scene(vbox));
		s.show();
		s.requestFocus();
	}
}
