/*
 * File: MoveType.java
 * Contributors: David McLain, Adrian Moore, Martin Cox, Luke Niemann
 * Description: This enum represents the different types of moves in our
 * game of Chess. Identifying the move types will help us know when to
 * perform auxiliary actions which don't apply to all moves, such as
 * moving the Rook when the King castles.
 */
public enum MoveType {
	NORMAL, CAPTURE, LONGCASTLE, SHORTCASTLE, DOUBLEMOVE, ENPASSANT
}
