package com.epitrack.guardioes.view.game.model;

public enum Phase {

    ARCH                    (1, 680, 5624, Piece.ARCH),
    ATHLETIC                (2, 980, 5564, Piece.ATHLETIC),
    BADMINTON               (3, 1200, 5452, Piece.BADMINTON),
    BASKETBALL              (4, 1332, 5288, Piece.BASKETBALL),
    BMX                     (5, 1288, 5092, Piece.BMX),
    BOXING                  (6, 1088, 4964, Piece.BOXING),
    SLALOM                  (7, 824, 4916, Piece.SLALOM),
    BOATING                 (8, 560, 4876, Piece.BOATING),
    CYCLING_ROAD            (9, 308, 4800, Piece.CYCLING_ROAD),
    CYCLING_TRACK           (10, 156, 4644, Piece.CYCLING_TRACK),
    FENCING                 (11, 198, 4398, Piece.FENCING),
    SOCCER                  (12, 432, 4300, Piece.SOCCER),
    ARTISTIC                (13, 676, 4250, Piece.ARTISTIC),
    GOLF                    (14, 926, 4210, Piece.GOLF),
    GRECO_ROMAN             (15, 1158, 4130, Piece.GRECO_ROMAN),
    HANDBALL                (16, 1300, 3984, Piece.HANDBALL),
    EQUESTRIAN_DRESSAGE     (17, 1316, 3800, Piece.EQUESTRIAN_DRESSAGE),
    EQUESTRIANISM           (18, 1176, 3664, Piece.EQUESTRIANISM),
    EQUESTRIAN_JUMPING      (19, 234, 3456, Piece.EQUESTRIAN_JUMPING),
    HOCKEY                  (20, 122, 3262, Piece.HOCKEY),
    JUDO                    (21, 188, 3100, Piece.JUDO),
    LIFTING                 (22, 406, 3038, Piece.LIFTING),
    WRESTLING               (23, 682, 3044, Piece.WRESTLING),
    AQUATIC_MARATHON        (24, 934, 3070, Piece.AQUATIC_MARATHON),
    MOUNTAIN_BIKE           (25, 1188, 3048, Piece.MOUNTAIN_BIKE),
    SYNCHRONIZED_SWIMMING   (26, 1332, 2916, Piece.SYNCHRONIZED_SWIMMING),
    SWIMMING                (27, 1324, 2720, Piece.SWIMMING),
    PENTATHLON              (28, 1150, 2608, Piece.PENTATHLON),
    WATER_POLO              (29, 926, 2576, Piece.WATER_POLO),
    ROWING                  (30, 692, 2598, Piece.ROWING),
    RHYTHMIC                (31, 454, 2556, Piece.RHYTHMIC),
    RUGBY                   (32, 222, 2498, Piece.RUGBY),
    ORNAMENTAL              (33, 160, 2334, Piece.ORNAMENTAL),
    TAEKWONDO               (34, 326, 2174, Piece.TAEKWONDO),
    TENNIS                  (35, 1260, 1944, Piece.TENNIS),
    TABLE_TENNIS            (36, 1310, 1686, Piece.TABLE_TENNIS),
    SHOOTING                (37, 1056, 1510, Piece.SHOOTING),
    TRAMPOLINE              (38, 546, 1454, Piece.TRAMPOLINE),
    TRIATHLON               (39, 204, 1372, Piece.TRIATHLON),
    SAIL                    (40, 262, 1136, Piece.SAIL),
    BEACH_VOLLEYBALL        (41, 598, 1084, Piece.BEACH_VOLLEYBALL),
    VOLLEYBALL              (42, 1186, 834, Piece.VOLLEYBALL),
    PHASE_43                (43, 746, 728, Piece.ARCH);

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
