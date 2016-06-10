package com.epitrack.guardioes.view.game.model;

public enum Phase {

    ARCH         (1, 680, 5624, Piece.ARCH),
    PHASE_2      (2, 980, 5564, Piece.ARCH),
    PHASE_3      (3, 1200, 5452, Piece.ARCH),
    PHASE_4      (4, 1332, 5288, Piece.ARCH),
    PHASE_5      (5, 1288, 5092, Piece.ARCH),
    PHASE_6      (6, 1088, 4964, Piece.ARCH),
    PHASE_7      (7, 824, 4916, Piece.ARCH),
    PHASE_8      (8, 560, 4876, Piece.ARCH),
    PHASE_9      (9, 308, 4800, Piece.ARCH),
    PHASE_10     (10, 156, 4644, Piece.ARCH),
    PHASE_11     (11, 198, 4398, Piece.ARCH),
    PHASE_12     (12, 432, 4300, Piece.ARCH),
    PHASE_13     (13, 676, 4250, Piece.ARCH),
    PHASE_14     (14, 926, 4210, Piece.ARCH),
    PHASE_15     (15, 1158, 4130, Piece.ARCH),
    PHASE_16     (16, 1300, 3984, Piece.ARCH),
    PHASE_17     (17, 1316, 3800, Piece.ARCH),
    PHASE_18     (18, 1176, 3664, Piece.ARCH),
    PHASE_19     (19, 234, 3456, Piece.ARCH),
    PHASE_20     (20, 122, 3262, Piece.ARCH),
    PHASE_21     (21, 188, 3100, Piece.ARCH),
    PHASE_22     (22, 406, 3038, Piece.ARCH),
    PHASE_23     (23, 682, 3044, Piece.ARCH),
    PHASE_24     (24, 934, 3070, Piece.ARCH),
    PHASE_25     (25, 1188, 3048, Piece.ARCH),
    PHASE_26     (26, 1332, 2916, Piece.ARCH),
    PHASE_27     (27, 1324, 2720, Piece.ARCH),
    PHASE_28     (28, 1150, 2608, Piece.ARCH),
    PHASE_29     (29, 926, 2576, Piece.ARCH),
    PHASE_30     (30, 692, 2598, Piece.ARCH),
    PHASE_31     (31, 454, 2556, Piece.ARCH),
    PHASE_32     (32, 222, 2498, Piece.ARCH),
    PHASE_33     (33, 160, 2334, Piece.ARCH),
    PHASE_34     (34, 326, 2174, Piece.ARCH),
    PHASE_35     (35, 1260, 1944, Piece.ARCH),
    PHASE_36     (36, 1310, 1686, Piece.ARCH),
    PHASE_37     (37, 1056, 1510, Piece.ARCH),
    PHASE_38     (38, 546, 1454, Piece.ARCH),
    PHASE_39     (39, 204, 1372, Piece.ARCH),
    PHASE_40     (40, 262, 1136, Piece.ARCH),
    PHASE_41     (41, 598, 1084, Piece.ARCH),
    PHASE_42     (42, 1186, 834, Piece.ARCH),
    PHASE_43     (43, 746, 728, Piece.ARCH);

    private static final int WIDTH = 1440;
    private static final int HEIGHT = 6446;

    private final int id;
    private final int x;
    private final int y;
    private final Integer[] pieceArray;

    Phase(final int id, final int x, final int y, final Integer[] pieceArray) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.pieceArray = pieceArray;
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

    public final Integer[] getPieceArray() {
        return pieceArray;
    }
}
