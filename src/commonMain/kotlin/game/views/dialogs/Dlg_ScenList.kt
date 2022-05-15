package game.views.dialogs

import com.github.servb.pph.pheroes.common.GfxId
import com.github.servb.pph.pheroes.common.TextResId
import com.github.servb.pph.pheroes.common.common.EMAP_FILE_HDR_KEY
import com.github.servb.pph.pheroes.common.common.GMAP_FILE_HDR_KEY
import game.logic.players.PlayerId
import com.github.servb.pph.pheroes.game.*
import com.github.servb.pph.util.SizeT
import com.github.servb.pph.util.asPoint
import com.github.servb.pph.util.deflate
import com.github.servb.pph.util.helpertype.UniqueValueEnum
import com.github.servb.pph.util.helpertype.getByValue
import com.soywiz.klogger.Logger
import com.soywiz.korma.geom.IRectangleInt
import com.soywiz.korma.geom.RectangleInt
import com.soywiz.korma.geom.SizeInt
import com.soywiz.korma.geom.y2
import game.logic.mapInfo.iMapInfo
import game.views.control.iDlgIconButton
import game.views.control.iPHScrollBar
import game.views.control.iTextButton
import gxlib.app.iViewMgr
import gxlib.graphics.dib.RGB16
import gxlib.graphics.dib.cColor
import gxlib.graphics.text.iTextComposer
import gxlib.utils.Alignment
import gxlib.utils.ReadU32
import gxlib.views.*
import rootVfs

typealias iScenList = MutableList<iMapInfo>

private val logger = Logger("Dlg_ScenListKt")

private suspend fun EnumScenarios(scList: iScenList) {
    rootVfs[gMapsPath].list().collect { file ->
        val mapInfo = iMapInfo()
        mapInfo.m_bNewGame = true
        mapInfo.m_FileName = file.path
        val pFile = file.openInputStream()
        val fourcc = pFile.ReadU32()
        if (fourcc == GMAP_FILE_HDR_KEY && mapInfo.ReadMapInfoPhm(pFile)) {
            scList.add(mapInfo)
        } else if (fourcc == EMAP_FILE_HDR_KEY && mapInfo.ReadMapInfoHmm(pFile)) {
            scList.add(mapInfo)
        } else {
            logger.warn { "Map '${mapInfo.m_FileName}' is not supported" }
        }
    }
}

class iScenListBox : iListBox {

    private val m_scList: List<iMapInfo>

    constructor(
            pViewMgr: iViewMgr,
            pCmdHandler: IViewCmdHandler,
            rect: IRectangleInt,
            uid: UInt,
            scList: List<iMapInfo>
    ) : super(pViewMgr, pCmdHandler, rect, uid) {
        m_scList = scList
    }

    override fun LBItemHeight(): SizeT = 15
    override fun LBItemsCount(): SizeT = m_scList.size

    override fun ComposeLBBackground(rect: IRectangleInt) {
        gApp.Surface().Darken25Rect(rect)
    }

    override fun ComposeLBItem(iIdx: SizeT, bSel: Boolean, irc: IRectangleInt) {
        val fc = iTextComposer.FontConfig(dlgfc_plain)
        val rc = RectangleInt(irc)

        ButtonFrame(gApp.Surface(), rc, iButton.State.Pressed.v)
        rc.rect.inflate(-1)
        if (bSel) {
            gGfxMgr.BlitTile(GfxId.PDGG_CTILE.v, gApp.Surface(), rc)
            ButtonFrame(gApp.Surface(), rc, 0)
        }

        rc.rect.inflate(-1)

        if (!m_scList[iIdx].Supported()) {
            fc.cmpProps.faceColor = RGB16(192, 160, 160)
        }

        // Map Size
        gTextComposer.TextOut(
            fc,
            gApp.Surface(),
            rc.asPoint(),
            gTextMgr[TextResId.TRID_SHORT_MAPSIZ_SMALL.v + m_scList[iIdx].m_Size.v],
            IRectangleInt(rc.x, rc.y, 20, rc.height),
            Alignment.AlignCenter
        )
        rc.rect.deflate(23, 0, 0, 0)

        // Players count
        gTextComposer.TextOut(
            fc,
            gApp.Surface(),
            rc.asPoint(),
            "${m_scList[iIdx].HumanPlayers()}/${m_scList[iIdx].TotalPlayers()}",
            IRectangleInt(rc.x, rc.y, 25, rc.height),
            Alignment.AlignCenter
        )
        rc.rect.deflate(25, 0, 0, 0)

        // Map name
        var title = m_scList[iIdx].m_Name
        if (m_scList[iIdx].m_Version.isNotBlank()) {
            title += " v.${m_scList[iIdx].m_Version}"
        }
        gTextComposer.TextOut(fc, gApp.Surface(), rc.asPoint(), title, rc, Alignment.AlignLeft)
    }
}

class iScenListDlg : iBaseGameDlg {

    private var m_selScen: Int
    private val m_scList: iScenList = mutableListOf()

    constructor(pViewMgr: iViewMgr) : super(pViewMgr, PlayerId.NEUTRAL) {
        m_selScen = -1
    }

    fun SelScen(): iMapInfo = m_scList[m_selScen]

    private enum class SortBy(override val v: Int, val comparator: (iMapInfo) -> Comparable<*>) : UniqueValueEnum {
        Size(0, { it.m_Size.v }),
        Players(1, { it.m_Players.size }),
        Name(2, { it.m_Name }),
    }

    private fun SortScenarios(sort_by: SortBy) {
        m_scList.sortWith(
            compareBy(
                iMapInfo::Supported,
                sort_by.comparator,
            )
        )
    }

    override suspend fun OnCreateDlg() {
        val clRect = ClientRect()

        EnumScenarios(m_scList)
        SortScenarios(SortBy.Name)

        // Listbox header
        AddChild(
            iDlgIconButton(
                m_pMgr,
                this,
                IRectangleInt(clRect.x, clRect.y + yoffs, 24, DEF_BTN_HEIGHT),
                GfxId.PDGG_BTN_MAPSIZE.v,
                501u
            )
        )
        AddChild(
            iDlgIconButton(
                m_pMgr,
                this,
                IRectangleInt(clRect.x + 25, clRect.y + yoffs, 24, DEF_BTN_HEIGHT),
                GfxId.PDGG_BTN_PLAYERS_COUNT.v,
                502u
            )
        )
        AddChild(
            iTextButton(
                m_pMgr,
                this,
                IRectangleInt(clRect.x + 50, clRect.y + yoffs, 280 - 16 - 50, DEF_BTN_HEIGHT),
                TextResId.TRID_MAP_NAME,
                503u
            )
        )

        // Listbox
        val pLB = iScenListBox(
            m_pMgr,
            this,
            IRectangleInt(clRect.x, clRect.y + yoffs + DEF_BTN_HEIGHT + 1, 280 - 16, 120),
            100u,
            m_scList
        )
        AddChild(pLB)
        // Scroll bar
        val pScrollBar = iPHScrollBar(
            m_pMgr,
            this,
            IRectangleInt(clRect.x + clRect.width - 15, clRect.y + yoffs, 15, 120 + DEF_BTN_HEIGHT + 1),
            300u
        )
        AddChild(pScrollBar)
        pLB.SetScrollBar(pScrollBar)

        // Buttons
        val npos = clRect.x + (clRect.width / 2 - 80)
        AddChild(
            iTextButton(
                m_pMgr,
                this,
                IRectangleInt(npos, clRect.y2 - DEF_BTN_HEIGHT, 50, DEF_BTN_HEIGHT),
                TextResId.TRID_OK,
                DLG_RETCODE.OK.v.toUInt(),
                ViewState.Visible.v
            )
        )
        AddChild(
            iTextButton(
                m_pMgr,
                this,
                IRectangleInt(npos + 55, clRect.y2 - DEF_BTN_HEIGHT, 50, DEF_BTN_HEIGHT),
                TextResId.TRID_INFO,
                301u,
                ViewState.Visible.v
            )
        )
        AddChild(
            iTextButton(
                m_pMgr,
                this,
                IRectangleInt(npos + 110, clRect.y2 - DEF_BTN_HEIGHT, 50, DEF_BTN_HEIGHT),
                TextResId.TRID_CANCEL,
                DLG_RETCODE.CANCEL.v.toUInt()
            )
        )

        // Init list
        SortScenarios(getByValue(gSettings.GetEntryValue(ConfigEntryType.NGDSORT)))
        if (m_scList.isNotEmpty()) {
            val selScen = gSettings.GetEntryValue(ConfigEntryType.NGDPOS).coerceIn(m_scList.indices)
            pLB.SetCurSel(selScen, true)
        }
    }

    override fun DoCompose(clRect: IRectangleInt) {
        gTextComposer.TextBoxOut(
            dlgfc_hdr,
            gApp.Surface(),
            gTextMgr[TextResId.TRID_SELECT_SCENARIO_DLG_HDR],
            IRectangleInt(clRect.x, clRect.y, clRect.width, 24)
        )
        gApp.Surface().FrameRect(
            IRectangleInt(clRect.x - 1, clRect.y - 1 + yoffs + DEF_BTN_HEIGHT + 1, 282 - 16, 120 + 2),
            cColor.Black.pixel
        )
    }

    override fun ClientSize(): SizeInt = SizeInt(280, 150 + DEF_BTN_HEIGHT + DEF_BTN_HEIGHT)

    override suspend fun iCMDH_ControlCommand(pView: iView, cmd: CTRL_CMD_ID, param: Int) {
        val uid = pView.GetUID().toInt()
        when (uid) {
            DLG_RETCODE.OK.v, DLG_RETCODE.CANCEL.v -> EndDialog(uid)
            301 -> {
                var title = m_scList[m_selScen].m_Name
                if (m_scList[m_selScen].m_Version.isNotBlank()) {
                    title += " v.${m_scList[m_selScen].m_Version}"
                }
                var desc = m_scList[m_selScen].m_Description
                if (m_scList[m_selScen].m_Author.isNotBlank()) {
                    desc += "\n\n${gTextMgr[TextResId.TRID_MAP_AUTHOR]}: ${m_scList[m_selScen].m_Author}"
                }
                val tdlg = iTextDlg(m_pMgr, title, desc, PlayerId.NEUTRAL, dlgfc_topic, dlgfc_splain)
                tdlg.DoModal()
            }
            in 501..503 -> {
                val nval = uid - 501
                gSettings.SetEntryValue(ConfigEntryType.NGDSORT, nval)
                SortScenarios(getByValue(nval))
                Invalidate()
            }
            100 -> {
                if (cmd == CTRL_CMD_ID.LBSELCHANGED) {
                    m_selScen = param
                    gSettings.SetEntryValue(ConfigEntryType.NGDPOS, param)
                    GetChildById(DLG_RETCODE.OK.v.toUInt())!!.SetEnabled(m_selScen != -1 && m_scList[m_selScen].Supported() && m_scList[m_selScen].HumanPlayers() > 0)
                    GetChildById(301u)!!.SetEnabled(m_selScen != -1 && m_scList[m_selScen].HumanPlayers() > 0)
                } else if (cmd == CTRL_CMD_ID.LBSELDBLCLICK) {
                    if (m_selScen != -1 && m_scList[m_selScen].Supported()) {
                        EndDialog(DLG_RETCODE.OK.v)
                    }
                }
            }
        }
    }

    private companion object {

        private const val yoffs = 18
    }
}
