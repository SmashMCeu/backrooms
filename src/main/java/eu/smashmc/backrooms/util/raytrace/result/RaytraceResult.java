package eu.smashmc.backrooms.util.raytrace.result;

import lombok.Data;

import java.util.List;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Data
public class RaytraceResult {

    private List<RaytraceHit> hits;
}
