package com.uni.lieferspatz.dto.api;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemsApi {
	private String restaurantImage;
	private String restaurantName;
	List<ItemApi> items;
}
