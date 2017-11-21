package ko.hyeonmin.boardpro.parts.Form.recyclerParts

/**
 * Created by junse on 2017-11-19.
 */
interface OnItemMoveListener {
    fun onItemMove(from: Int, to: Int)
    fun onItemDropped(from: Int, to: Int)
}
