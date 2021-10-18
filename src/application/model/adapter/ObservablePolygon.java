package application.model.adapter;

import java.util.logging.Level;

import application.model.algorithms.Polygon;
import application.util.LogsUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ObservablePolygon extends Polygon {

	private StringProperty boundaryWordProperty;

	public ObservablePolygon() {
		this("");
	}

	// TODO S'occuper de l'exception
	public ObservablePolygon(String boundaryWord) {
		super("");
		boundaryWordProperty = new SimpleStringProperty();
		boundaryWordProperty.addListener(
				(observable, oldValue, newValue) -> {
					try {
						BoundaryParser bp = new BoundaryParser(newValue);
						String word = bp.parse();
						super.initialize(word);
					} catch (BoundaryParserException e) {
						LogsUtil.logger.log(Level.WARNING, "", e);
					}
				});
		setBoundaryWord(boundaryWord);
	}

	public StringProperty boundaryWordProperty() {
		return boundaryWordProperty;
	}

	public String getBoundaryWord() {
		return boundaryWordProperty.get();
	}

	public void setBoundaryWord(String boundaryWord) {
		boundaryWordProperty.set(boundaryWord);
	}
}
