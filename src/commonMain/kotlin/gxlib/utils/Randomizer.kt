package gxlib.utils

import kotlin.properties.Delegates

interface IiRandomizer {

    fun GetSeed(): UInt
}

class iRandomizer(seed: UInt = 1u) : IiRandomizer {

    private var m_holdrand by Delegates.notNull<UInt>()

    init {
        SetNewSeed(seed)
    }

    fun SetNewSeed(seed: UInt): UInt {
        m_holdrand = seed
        return m_holdrand
    }

    override fun GetSeed(): UInt = m_holdrand

    fun Rand(maxVal: Int = MaxVal): Int {
        require(maxVal > 0)

        val newHoldrand = m_holdrand * 214013u + 2531011u
        var res = (newHoldrand shr 16).toInt() and 0x7FFF
        m_holdrand = newHoldrand
        if (maxVal != MaxVal) {
            res %= maxVal
        }
        return res
    }

    fun Rand(minVal: Int, maxVal: Int): Int {
        require(minVal < maxVal)
        return minVal + Rand(maxVal - minVal)
    }

    companion object {

        const val MaxVal = 0x7FFF
    }
}