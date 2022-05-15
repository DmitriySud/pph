package game.views.dialogs

import com.github.servb.pph.pheroes.common.TextResId
import com.github.servb.pph.pheroes.game.*
import game.logic.players.PlayerId
import com.github.servb.pph.util.asRectangle
import com.soywiz.korma.geom.IRectangleInt
import com.soywiz.korma.geom.RectangleInt
import com.soywiz.korma.geom.SizeInt
import com.soywiz.korma.geom.y2
import com.soywiz.korma.math.clamp
import game.views.control.iTextButton
import gxlib.app.iViewMgr
import gxlib.graphics.text.iTextComposer
import gxlib.views.*

abstract class iBaseGameDlg : iDialog, IViewCmdHandler {

    private val m_pid: PlayerId

    constructor(pViewMgr: iViewMgr, pid: PlayerId) : super(pViewMgr) {
        m_pid = pid
    }

    override fun `$destruct`() {}

    fun ClientRect(): RectangleInt {
        val rc = RectangleInt(GetDialogMetrics().asRectangle())
        rc.rect.inflate(-DLG_FRAME_SIZE)
        return rc
    }

    fun GetMaxClientSize(): SizeInt {
        val res = gApp.Surface().GetSize()
        res.width -= DLG_FRAME_SIZE * 2
        res.height -= DLG_FRAME_SIZE * 2
        return res
    }

    abstract fun DoCompose(clRect: IRectangleInt)
    abstract fun ClientSize(): SizeInt

    final override fun OnCompose() {
        ComposeDlgBkgnd(gApp.Surface(), m_Rect, m_pid, true)
        val rc = RectangleInt(m_Rect)
        rc.rect.inflate(-DLG_FRAME_SIZE)
        DoCompose(rc)
    }

    final override fun GetDialogMetrics(): SizeInt {
        val siz = ClientSize()
        val max = gApp.Surface().GetSize()
        siz.width = (siz.width + DLG_FRAME_SIZE * 2).clamp(80, max.width - 6)
        siz.height = (siz.height + DLG_FRAME_SIZE * 2).clamp(40, max.height - 6)
        return siz
    }
}

/** Base text dialog MB_OK. */
open class iTextDlg : iBaseGameDlg {

    protected val m_fcTitle: iTextComposer.IFontConfig
    protected val m_fcText: iTextComposer.IFontConfig
    protected val m_title: String
    protected var m_text: String

    constructor(
            pViewMgr: iViewMgr,
            title: String,
            text: String,
            pid: PlayerId,
            fc_title: iTextComposer.IFontConfig = dlgfc_hdr,
            fc_text: iTextComposer.IFontConfig = dlgfc_plain
    ) : super(pViewMgr, pid) {
        m_title = title
        m_text = text
        m_fcTitle = fc_title
        m_fcText = fc_text
    }

    override fun `$destruct`() {}

    override suspend fun OnCreateDlg() {
        val clRect = ClientRect()
        AddChild(
            iTextButton(
                m_pMgr,
                this,
                IRectangleInt(clRect.x + (clRect.width / 2 - 20), clRect.y2 - DEF_BTN_HEIGHT, 40, DEF_BTN_HEIGHT),
                TextResId.TRID_OK,
                DLG_RETCODE.OK.v.toUInt()
            )
        )
    }

    fun SetText(text: String) {
        m_text = text
    }

    override fun DoCompose(clRect: IRectangleInt) {
        val rc = RectangleInt(clRect)

        if (m_title.isNotEmpty()) {
            val h = gTextComposer.TextBoxOut(m_fcTitle, gApp.Surface(), m_title, rc)
            rc.y += h
            rc.y += 10
        }

        if (m_text.isNotEmpty()) {
            val h = gTextComposer.TextBoxOut(m_fcText, gApp.Surface(), m_text, rc)
            rc.y += h  // todo: doesn't do anything
//            h += 10  // todo: why is this in sources?
        }
    }

    override fun ClientSize(): SizeInt {
        var w = 130
        if (m_text.length > 20) {
            w += (m_text.length / 4).clamp(0, 100)
        }
        var h = 0

        if (m_title.isNotEmpty()) {
            val s = gTextComposer.GetTextBoxSize(m_title, w, m_fcTitle)
            h += s.height
            h += 10
        }

        if (m_text.isNotEmpty()) {
            val s = gTextComposer.GetTextBoxSize(m_text, w, m_fcText)
            h += s.height
            h += 10
        }

        h += DEF_BTN_HEIGHT  // OK button

        return SizeInt(w, h)
    }

    final override suspend fun iCMDH_ControlCommand(pView: iView, cmd: CTRL_CMD_ID, param: Int) {
        val uid = pView.GetUID()
        if (cmd == CTRL_CMD_ID.BUTTON_CLICK) {
            EndDialog(uid.toInt())
        }
    }
}

/** Extended text dialog MB_OK. */
abstract class iExtTextDlg : iBaseGameDlg {

    protected val m_title: String
    protected val m_text: String
    protected val m_sign: String

    constructor(pViewMgr: iViewMgr, title: String, text: String, sign: String, pid: PlayerId) : super(pViewMgr, pid) {
        m_title = title
        m_text = text
        m_sign = sign
    }

    override fun `$destruct`() {}

    override suspend fun OnCreateDlg() {
        val clrRect = ClientRect()
        TODO("AddChild(new iTextButton(m_pMgr,this,iRect(clRect.x+(clRect.w/2-20),clRect.y2()-DEF_BTN_HEIGHT,40,DEF_BTN_HEIGHT),TRID_OK, DRC_OK))")
    }

    override fun DoCompose(clRect: IRectangleInt) {
        val rc = RectangleInt(clRect)

        if (m_title.isNotEmpty()) {
            val h = gTextComposer.TextBoxOut(dlgfc_hdr, gApp.Surface(), m_title, rc)
            rc.y += h
            rc.y += 10
        }

        if (m_text.isNotEmpty()) {
            val h = gTextComposer.TextBoxOut(dlgfc_plain, gApp.Surface(), m_text, rc)
            rc.y += h
//            h += 10;  // todo: why is this in sources?
        }

        if (m_sign.isNotEmpty()) {
            rc.y += 5
            val h = gTextComposer.TextBoxOut(dlgfc_topic, gApp.Surface(), m_sign, rc)
            rc.y += h  // todo: doesn't do anything
//            h += 5;  // todo: why is this in sources?
        }
    }

    override fun ClientSize(): SizeInt {
        var w = 130
        if (m_text.length > 20) {
            w += (m_text.length / 4).clamp(0, 70)
        }
        var h = 0

        if (m_title.isNotEmpty()) {
            val s = gTextComposer.GetTextBoxSize(m_title, w, dlgfc_hdr)
            h += s.height
            h += 10
        }

        if (m_text.isNotEmpty()) {
            val s = gTextComposer.GetTextBoxSize(m_text, w, dlgfc_plain)
            h += s.height
            h += 10
        }

        if (m_sign.isNotEmpty()) {
            val s = gTextComposer.GetTextBoxSize(m_sign, w, dlgfc_topic)
            h += s.height
            h += 10
        }

        h += DEF_BTN_HEIGHT  // OK button

        return SizeInt(w, h)
    }

    final override suspend fun iCMDH_ControlCommand(pView: iView, cmd: CTRL_CMD_ID, param: Int) {
        val uid = pView.GetUID()
        if (cmd == CTRL_CMD_ID.BUTTON_CLICK) {
            EndDialog(uid.toInt())
        }
    }
}

/** Base text with icon dialog MB_OK. */
abstract class iIconDlg : iTextDlg {

    protected val m_sid: SpriteId

    constructor(pViewMgr: iViewMgr, title: String, text: String, sid: SpriteId, pid: PlayerId) : super(
        pViewMgr,
        title,
        text,
        pid
    ) {
        m_sid = sid
    }

    override fun `$destruct`() {}

    override fun DoCompose(clRect: IRectangleInt) {
        val rc = RectangleInt(clRect)
        super.DoCompose(clRect)
        TODO()
//        val ssiz = gGfxMgr.Dimension(m_sid)...
    }

    override fun ClientSize(): SizeInt {
        val res = super.ClientSize()
        TODO("implement gGfxMgr and do actions here")
    }
}

/** Question dialog MB_YESNO. */
class iQuestDlg : iTextDlg {

    constructor(pViewMgr: iViewMgr, title: String, text: String, pid: PlayerId) : super(pViewMgr, title, text, pid)

    override fun `$destruct`() {}

    override suspend fun OnCreateDlg() {
        val clRect = ClientRect()
        val npos = clRect.x + (clRect.width / 2 - 54)

        AddChild(
            iTextButton(
                m_pMgr,
                this,
                IRectangleInt(npos, clRect.y2 - DEF_BTN_HEIGHT, 40, DEF_BTN_HEIGHT),
                TextResId.TRID_YES,
                DLG_RETCODE.YES.v.toUInt()
            )
        )
        AddChild(
            iTextButton(
                m_pMgr,
                this,
                IRectangleInt(npos + 50, clRect.y2 - DEF_BTN_HEIGHT, 40, DEF_BTN_HEIGHT),
                TextResId.TRID_NO,
                DLG_RETCODE.NO.v.toUInt()
            )
        )
    }
}
