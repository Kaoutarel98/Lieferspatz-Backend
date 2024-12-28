package com.uni.lieferspatz.constants;

import lombok.Getter;

@Getter
public enum BestellStatus {
	BEARBEITUNG(1), ZUBEREITUNG(2), ABGESCHLOSSEN(3), STORNIERT(4);

	private final int priority;

	BestellStatus(int priority) {
		this.priority = priority;
	}

	public static BestellStatus fromString(String status) {
		return switch (status.toUpperCase()) {
			case "BEARBEITUNG" -> BEARBEITUNG;
			case "ZUBEREITUNG" -> ZUBEREITUNG;
			case "ABGESCHLOSSEN" -> ABGESCHLOSSEN;
			case "STORNIERT" -> STORNIERT;
			default -> BEARBEITUNG;
		};
	}

	public static int getPriority(String status) {
		return fromString(status).getPriority();
	}
}
