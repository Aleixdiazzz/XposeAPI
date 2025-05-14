package com.aleix.XposeAPI.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CollectionsPublicRS {

    private Serie serie;
    private List<Asset> assets;
    private String imageUrl;


}
