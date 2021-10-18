package application.model.adapter;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "polygons")
public class WrapperPolygonList {

	private List<ObservablePolygon> polygons;

	@XmlElement(name = "polygon")
	public List<ObservablePolygon> getPolygons() {
		return polygons;
	}

	public void setPolygons(List<ObservablePolygon> polygons) {
		this.polygons = polygons;
	}
}
