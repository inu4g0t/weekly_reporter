package javafx.component;
import java.io.Serializable;

import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;


public class TextFieldTreeCellImpl extends TreeCell<String> implements Serializable {

	private TextField textField;

	public TextFieldTreeCellImpl() {
		this.setId(this.getClass().getSimpleName() + System.currentTimeMillis());
	}

	@Override
	public void startEdit() {
		super.startEdit();

		if (textField == null) {
			createTextField();
		}
		setText(null);
		textField.setText(getString());
		setGraphic(textField);
		textField.selectAll();
	}

	@Override
	public void cancelEdit() {
		super.cancelEdit();
		setText((String) getItem());
		setGraphic(getTreeItem().getGraphic());
	}

	@Override
	public void updateItem(String item, boolean empty) {
		super.updateItem(item, empty);

		if (empty) {
			setText(null);
			setGraphic(null);
		} else {
			if (isEditing()) {
				if (textField != null) {
					textField.setText(getString());
				}
				setText(null);
				setGraphic(textField);
			} else {
				setText(getString());
				setGraphic(getTreeItem().getGraphic());
			}
		}
	}

	private void createTextField() {
		textField = new TextField(getString());
		textField.setOnKeyReleased((KeyEvent t) -> {
			if (t.getCode() == KeyCode.ENTER) {
				commitEdit(textField.getText());
			} else if (t.getCode() == KeyCode.ESCAPE) {
				cancelEdit();
			}
		});
	}

	private String getString() {
		return getItem() == null ? "" : getItem().toString();
	}
	
}