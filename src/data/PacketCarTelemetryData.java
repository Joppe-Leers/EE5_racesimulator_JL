package data;


import data.elements.ButtonStatus;
import data.elements.CarTelemetryData;

import java.util.List;

//import gui.codegeek.RealtimeInt;

public class PacketCarTelemetryData extends Packet {

	private List<CarTelemetryData> carTelemetryData;
	private ButtonStatus buttonStatus;

	public PacketCarTelemetryData() {

	}


	public List<CarTelemetryData> getCarTelemetryData() {
		return carTelemetryData;
	}
	public void setCarTelemetryData(List<CarTelemetryData> carTelemetryData) {
		this.carTelemetryData = carTelemetryData;
	}

	public ButtonStatus getButtonStatus() {
		return buttonStatus;
	}

	public void setButtonStatus(ButtonStatus buttonStatus) {
		this.buttonStatus = buttonStatus;
	}

}
