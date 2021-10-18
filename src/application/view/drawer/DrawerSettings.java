package application.view.drawer;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class DrawerSettings {

	BooleanProperty drawGridProperty;
	BooleanProperty drawCheckerBoardProperty;
	BooleanProperty drawHeightFunctionProperty;
	BooleanProperty drawTilingProperty;
	BooleanProperty drawColoredTilingProperty;

	int padx = 40;
	int pady = 40;

	Paint backgroundColor = Color.WHITE;
	Paint checkerboardColor = Color.GREY;
	Paint gridColor = Color.BLACK;
	double gridLineWidth = 1.0;
	Paint polygonColor = Color.BLUE;
	double polygonLineWidth = 5.0;
	Paint heightColor = Color.RED;
	double heightLineWidth = 1.0;
	Paint tilingColor1 = Color.YELLOW;
	Paint tilingColor2 = Color.GREEN;
	Paint tilingColor3 = Color.RED;
	Paint tilingColor4 = Color.BLUE;

	DrawerSettings(Drawer drawer) {
		ChangeListener<? super Boolean> drawListener = (
				(observable, oldValue, newValue) -> drawer.draw()
				);

		drawGridProperty = new SimpleBooleanProperty(true);
		drawCheckerBoardProperty = new SimpleBooleanProperty(false);
		drawHeightFunctionProperty = new SimpleBooleanProperty(false);
		drawTilingProperty = new SimpleBooleanProperty(false);
		drawColoredTilingProperty = new SimpleBooleanProperty(false);

		drawGridProperty.addListener(drawListener);
		drawCheckerBoardProperty.addListener(drawListener);
		drawHeightFunctionProperty.addListener(drawListener);
		drawTilingProperty.addListener(drawListener);
		drawColoredTilingProperty.addListener(drawListener);
	}

	public BooleanProperty drawCheckerBoardProperty() {
		return drawCheckerBoardProperty;
	}

	public BooleanProperty drawColoredTilingProperty() {
		return drawColoredTilingProperty;
	}

	public BooleanProperty drawGridProperty() {
		return drawGridProperty;
	}
	public BooleanProperty drawHeightFunctionProperty() {
		return drawHeightFunctionProperty;
	}
	public BooleanProperty drawTilingProperty() {
		return drawTilingProperty;
	}
	public Paint getBackgroundColor() {
		return backgroundColor;
	}
	public Paint getCheckerboardColor() {
		return checkerboardColor;
	}

	public Paint getGridColor() {
		return gridColor;
	}

	public double getGridLineWidth() {
		return gridLineWidth;
	}

	public Paint getHeightColor() {
		return heightColor;
	}

	public double getHeightLineWidth() {
		return heightLineWidth;
	}

	public int getPadx() {
		return padx;
	}

	public int getPady() {
		return pady;
	}

	public Paint getPolygonColor() {
		return polygonColor;
	}

	public double getPolygonLineWidth() {
		return polygonLineWidth;
	}

	Paint getTilingColor1() {
		return tilingColor1;
	}

	Paint getTilingColor2() {
		return tilingColor2;
	}

	Paint getTilingColor3() {
		return tilingColor3;
	}

	Paint getTilingColor4() {
		return tilingColor4;
	}

	public void setBackgroundColor(Paint backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public void setCheckerboardColor(Paint checkerboardColor) {
		this.checkerboardColor = checkerboardColor;
	}

	public void setGridColor(Paint gridColor) {
		this.gridColor = gridColor;
	}

	public void setGridLineWidth(double gridLineWidth) {
		this.gridLineWidth = gridLineWidth;
	}


	public void setHeightColor(Paint heightColor) {
		this.heightColor = heightColor;
	}

	public void setHeightLineWidth(double heightLineWidth) {
		this.heightLineWidth = heightLineWidth;
	}

	public void setPadx(int padx) {
		this.padx = padx;
	}

	public void setPady(int pady) {
		this.pady = pady;
	}

	public void setPolygonColor(Paint polygonColor) {
		this.polygonColor = polygonColor;
	}

	public void setPolygonLineWidth(double polygonLineWidth) {
		this.polygonLineWidth = polygonLineWidth;
	}

	void setTilingColor1(Paint tilingColor1) {
		this.tilingColor1 = tilingColor1;
	}

	void setTilingColor2(Paint tilingColor2) {
		this.tilingColor2 = tilingColor2;
	}

	void setTilingColor3(Paint tilingColor3) {
		this.tilingColor3 = tilingColor3;
	}

	void setTilingColor4(Paint tilingColor4) {
		this.tilingColor4 = tilingColor4;
	}
}