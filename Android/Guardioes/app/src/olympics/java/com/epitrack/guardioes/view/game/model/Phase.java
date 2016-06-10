package com.epitrack.guardioes.view.game.model;

public enum Phase {

    PHASE_1      (1, 680, 5624),
    PHASE_2      (2, 980, 5564),
    PHASE_3      (3, 1200, 5452),
    PHASE_4      (4, 1332, 5288),
    PHASE_5      (5, 1288, 5092),
    PHASE_6      (6, 1088, 4964),
    PHASE_7      (7, 824, 4916),
    PHASE_8      (8, 560, 4876),
    PHASE_9      (9, 308, 4800),
    PHASE_10     (10, 156, 4644),
    PHASE_11     (11, 198, 4398),
    PHASE_12     (12, 432, 4300),
    PHASE_13     (13, 676, 4250),
    PHASE_14     (14, 926, 4210),
    PHASE_15     (15, 1158, 4130),
    PHASE_16     (16, 1300, 3984),
    PHASE_17     (17, 1316, 3800),
    PHASE_18     (18, 500, 500),
    PHASE_19     (19, 1176, 3664),
    PHASE_20     (20, 234, 3456),
    PHASE_21     (21, 122, 3262),
    PHASE_22     (22, 188, 3100),
    PHASE_23     (23, 406, 3038),
    PHASE_24     (24, 682, 3044),
    PHASE_25     (25, 934, 3070),
    PHASE_26     (26, 1188, 3048),
    PHASE_27     (27, 1332, 2916),
    PHASE_28     (28, 1324, 2720),
    PHASE_29     (29, 1150, 2608),
    PHASE_30     (30, 926, 2576),
    PHASE_31     (31, 692, 2598),
    PHASE_32     (32, 454, 2556),
    PHASE_33     (33, 222, 2498),
    PHASE_34     (34, 160, 2334),
    PHASE_35     (35, 326, 2174),
    PHASE_36     (36, 1260, 1944),
    PHASE_37     (37, 1310, 1686),
    PHASE_38     (38, 1056, 1510),
    PHASE_39     (39, 546, 1454),
    PHASE_40     (40, 204, 1372),
    PHASE_41     (41, 262, 1136),
    PHASE_42     (42, 598, 1084),
    PHASE_43     (43, 1186, 834),
    PHASE_44     (44, 746, 728);

    private static final int WIDTH = 1440;
    private static final int HEIGHT = 6446;

    private final int id;
    private final int x;
    private final int y;

    Phase(final int id, final int x, final int y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public final int getId() {
        return id;
    }

    public final int getX(final int width) {
        return x * width / WIDTH;
    }

    public final int getY(final int height) {
        return y * height / HEIGHT;
    }
}
