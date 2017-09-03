package customUnits;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import models.SQLTableColumn;

public class ColumnListCell extends ListCell<SQLTableColumn> {

	HBox hbox = new HBox();
	Label name = new Label("");
	Label type = new Label("");
	Pane pane = new Pane();
	Button button = new Button("Remove");

	public ColumnListCell() {
		super();

		type.setOpacity(0.7);

		hbox.getChildren().addAll(name,type, pane, button);
		hbox.setSpacing(15);

        HBox.setHgrow(pane, Priority.ALWAYS);
        button.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				getListView().getItems().remove(getItem());
			}
		});
	}

	@Override
	protected void updateItem(SQLTableColumn item, boolean empty) {
		super.updateItem(item, empty);
		setText(null);
		setGraphic(null);

		if (item != null && !empty) {
			name.setText(item.getName());

			if (item.isNotNull())
				type.setText(item.getType().getName() + " NOT NULL");
			else
				type.setText(item.getType().getName());

			if (item.getName().equals("ID")){
				button.setVisible(false);
			}
			setGraphic(hbox);
		}
	}

}
