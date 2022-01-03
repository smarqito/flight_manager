package g12.Server;

import java.io.IOException;

import g12.Middleware.*;
import g12.Server.FlightManager.*;

public class ServerWorker implements Runnable {

	private final TaggedConnection c;
	private final IFlightManager model;

	/**
	 * 
	 * @param c
	 * @param model
	 */
	public ServerWorker(TaggedConnection c, IFlightManager model) {
		this.model = model;
		this.c = c;
	}

	@Override
	public void run() {
		try {
			try (c) {
				try {
					for (;;) {
						TaggedConnection.Frame frame = this.c.receive();
						this.requestHandler(frame);
					}
				} catch (IOException ignored) {
				}
			}
		} catch (Exception alsoignored) {
			// faz nada
		}
	}

	public void requestHandler(TaggedConnection.Frame f) {
		try {
			IFlightManager.class.getMethod("login");
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
	}

	public void loginHandler() {

	}

	public void registerUser() {

	}

	public void registerFlight() {

	}

	public void closeDay() {

	}

	public void bookFlight() {

	}

	public void cancelBook() {

	}

	public void availableFlights() {

	}

}