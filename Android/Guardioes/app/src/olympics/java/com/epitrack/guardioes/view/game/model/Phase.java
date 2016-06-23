package com.epitrack.guardioes.view.game.model;

import com.epitrack.guardioes.R;

public enum Phase {

    ARCH                    (1, 680, 5624, R.string.about, R.drawable.image_arch, Piece.ARCH),
    ATHLETIC                (2, 980, 5564, R.string.about, R.drawable.image_athletics, Piece.ATHLETIC),
    BADMINTON               (3, 1200, 5452, R.string.about, R.drawable.image_badminton, Piece.BADMINTON),
    BASKETBALL              (4, 1332, 5288, R.string.about, R.drawable.image_basketball, Piece.BASKETBALL),
    BMX                     (5, 1288, 5092, R.string.about, R.drawable.image_bmx, Piece.BMX),
    BOXING                  (6, 1088, 4964, R.string.about, R.drawable.image_boxing, Piece.BOXING),
    SLALOM                  (7, 824, 4916, R.string.about, R.drawable.image_slalom, Piece.SLALOM),
    BOATING                 (8, 560, 4876, R.string.about, R.drawable.image_boating, Piece.BOATING),
    CYCLING_ROAD            (9, 308, 4800, R.string.about, R.drawable.image_cycling_road, Piece.CYCLING_ROAD),
    CYCLING_TRACK           (10, 156, 4644, R.string.about, R.drawable.image_cycling_track, Piece.CYCLING_TRACK),
    FENCING                 (11, 198, 4398, R.string.about, R.drawable.image_fencing, Piece.FENCING),
    SOCCER                  (12, 432, 4300, R.string.about, R.drawable.image_soccer, Piece.SOCCER),
    ARTISTIC                (13, 676, 4250, R.string.about, R.drawable.image_artistic, Piece.ARTISTIC),
    GOLF                    (14, 926, 4210, R.string.about, R.drawable.image_golf, Piece.GOLF),
    GRECO_ROMAN             (15, 1158, 4130, R.string.about, R.drawable.image_greco_roman, Piece.GRECO_ROMAN),
    HANDBALL                (16, 1300, 3984, R.string.about, R.drawable.image_handball, Piece.HANDBALL),
    EQUESTRIAN_DRESSAGE     (17, 1316, 3800, R.string.about, R.drawable.image_equestrian_dressage, Piece.EQUESTRIAN_DRESSAGE),
    EQUESTRIANISM           (18, 1176, 3664, R.string.about, R.drawable.image_equestrianism, Piece.EQUESTRIANISM),
    EQUESTRIAN_JUMPING      (19, 234, 3456, R.string.about, R.drawable.image_equestrian_jumping, Piece.EQUESTRIAN_JUMPING),
    HOCKEY                  (20, 122, 3262, R.string.about, R.drawable.image_hockey, Piece.HOCKEY),
    JUDO                    (21, 188, 3100, R.string.about, R.drawable.image_judo, Piece.JUDO),
    LIFTING                 (22, 406, 3038, R.string.about, R.drawable.image_lifting, Piece.LIFTING),
    WRESTLING               (23, 682, 3044, R.string.about, R.drawable.image_wrestling, Piece.WRESTLING),
    AQUATIC_MARATHON        (24, 934, 3070, R.string.about, R.drawable.image_aquatic_marathon, Piece.AQUATIC_MARATHON),
    MOUNTAIN_BIKE           (25, 1188, 3048, R.string.about, R.drawable.image_mountain_bike, Piece.MOUNTAIN_BIKE),
    SYNCHRONIZED_SWIMMING   (26, 1332, 2916, R.string.about, R.drawable.image_synchronized_swimming, Piece.SYNCHRONIZED_SWIMMING),
    SWIMMING                (27, 1324, 2720, R.string.about, R.drawable.image_swimming, Piece.SWIMMING),
    PENTATHLON              (28, 1150, 2608, R.string.about, R.drawable.image_pentathlon, Piece.PENTATHLON),
    WATER_POLO              (29, 926, 2576, R.string.about, R.drawable.image_water_polo, Piece.WATER_POLO),
    ROWING                  (30, 692, 2598, R.string.about, R.drawable.image_rowing, Piece.ROWING),
    RHYTHMIC                (31, 454, 2556, R.string.about, R.drawable.image_rhythmic, Piece.RHYTHMIC),
    RUGBY                   (32, 222, 2498, R.string.about, R.drawable.image_rugby, Piece.RUGBY),
    ORNAMENTAL              (33, 160, 2334, R.string.about, R.drawable.image_ornamental, Piece.ORNAMENTAL),
    TAEKWONDO               (34, 326, 2174, R.string.about, R.drawable.image_taekwondo, Piece.TAEKWONDO),
    TENNIS                  (35, 1260, 1944, R.string.about, R.drawable.image_tennis, Piece.TENNIS),
    TABLE_TENNIS            (36, 1310, 1686, R.string.about, R.drawable.image_table_tennis, Piece.TABLE_TENNIS),
    SHOOTING                (37, 1056, 1510, R.string.about, R.drawable.image_shooting, Piece.SHOOTING),
    TRAMPOLINE              (38, 546, 1454, R.string.about, R.drawable.image_trampoline, Piece.TRAMPOLINE),
    TRIATHLON               (39, 204, 1372, R.string.about, R.drawable.image_triathlon, Piece.TRIATHLON),
    SAIL                    (40, 262, 1136, R.string.about, R.drawable.image_sail, Piece.SAIL),
    BEACH_VOLLEYBALL        (41, 598, 1084, R.string.about, R.drawable.image_beach_volleyball, Piece.BEACH_VOLLEYBALL),
    VOLLEYBALL              (42, 1186, 834, R.string.about, R.drawable.image_volleyball, Piece.VOLLEYBALL),
    PHASE_43                (43, 746, 728, R.string.about, R.drawable.image_arch, Piece.ARCH);

    private static final int WIDTH = 1440;
    private static final int HEIGHT = 6446;

    private final int id;
    private final int x;
    private final int y;
    private final int name;
    private final int image;
    private final Integer[] pieceArray;

    Phase(final int id, final int x, final int y, final int name, final int image, final Integer[] pieceArray) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.name = name;
        this.image = image;
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

    public int getName() {
        return name;
    }

    public final int getImage() {
        return image;
    }

    public final Integer[] getPieceArray() {
        return pieceArray;
    }
}
