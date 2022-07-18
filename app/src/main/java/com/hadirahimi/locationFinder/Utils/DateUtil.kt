package com.hadirahimi.locationFinder.Utils

import javax.inject.Inject

class DateUtil @Inject constructor()
{
    var day = 0
        private set
    
    var month = 0
        private set
    
    var year = 0
        private set
    private var jY = 0
    private var jM = 0
    private var jD = 0
    private var gY = 0
    private var gM = 0
    private var gD = 0
    private var leap = 0
    private var march = 0
    
    
    private fun JG2JD(year : Int , month : Int , day : Int , J1G0 : Int) : Int
    {
        var jd =
            (1461 * (year + 4800 + (month - 14) / 12) / 4 + 367 * (month - 2 - 12 * ((month - 14) / 12)) / 12 - 3 * ((year + 4900 + (month - 14) / 12) / 100) / 4 + day - 32075)
        if (J1G0 == 0) jd = jd - (year + 100100 + (month - 8) / 6) / 100 * 3 / 4 + 752
        return jd
    }
    
    private fun JD2JG(JD : Int , J1G0 : Int)
    {
        val i : Int
        var j : Int
        j = 4 * JD + 139361631
        if (J1G0 == 0)
        {
            j = j + (4 * JD + 183187720) / 146097 * 3 / 4 * 4 - 3908
        }
        i = j % 1461 / 4 * 5 + 308
        gD = i % 153 / 5 + 1
        gM = i / 153 % 12 + 1
        gY = j / 1461 - 100100 + (8 - gM) / 6
    }
    
    private fun JD2Jal(JDN : Int)
    {
        JD2JG(JDN , 0)
        jY = gY - 621
        JalCal(jY)
        val JDN1F = JG2JD(gY , 3 , march , 0)
        var k = JDN - JDN1F
        if (k >= 0)
        {
            if (k <= 185)
            {
                jM = 1 + k / 31
                jD = k % 31 + 1
                return
            }
            else
            {
                k = k - 186
            }
        }
        else
        {
            jY = jY - 1
            k = k + 179
            if (leap == 1) k = k + 1
        }
        jM = 7 + k / 30
        jD = k % 30 + 1
    }
    
    private fun Jal2JD(jY : Int , jM : Int , jD : Int) : Int
    {
        JalCal(jY)
        val jd = JG2JD(gY , 3 , march , 1) + (jM - 1) * 31 - jM / 7 * (jM - 7)
        + jD - 1
        return jd
    }
    
    private fun JalCal(jY : Int)
    {
        march = 0
        leap = 0
        val breaks = intArrayOf(
            - 61 ,
            9 ,
            38 ,
            199 ,
            426 ,
            686 ,
            756 ,
            818 ,
            1111 ,
            1181 ,
            1210 ,
            1635 ,
            2060 ,
            2097 ,
            2192 ,
            2262 ,
            2324 ,
            2394 ,
            2456 ,
            3178
        )
        gY = jY + 621
        var leapJ = - 14
        var jp = breaks[0]
        var jump = 0
        for (j in 1 .. 19)
        {
            val jm = breaks[j]
            jump = jm - jp
            if (jY < jm)
            {
                var N = jY - jp
                leapJ = leapJ + N / 33 * 8 + (N % 33 + 3) / 4
                if (jump % 33 == 4 && jump - N == 4) leapJ = leapJ + 1
                val leapG = gY / 4 - (gY / 100 + 1) * 3 / 4 - 150
                march = 20 + leapJ - leapG
                if (jump - N < 6) N = N - jump + (jump + 4) / 33 * 33
                leap = ((N + 1) % 33 - 1) % 4
                if (leap == - 1) leap = 4
                break
            }
            leapJ = leapJ + jump / 33 * 8 + jump % 33 / 4
            jp = jm
        }
    }
    
    
    override fun toString() : String
    {
        return String.format("%04d-%02d-%02d" , year , month , day)
    }
    
    fun GregorianToPersian(year : Int , month : Int , day : Int)
    {
        val jd = JG2JD(year , month , day , 0)
        JD2Jal(jd)
        this.year = jY
        this.month = jM
        this.day = jD
    }
    
    
    fun PersianToGregorian(year : Int , month : Int , day : Int)
    {
        val jd = Jal2JD(year , month , day)
        JD2JG(jd , 0)
        this.year = gY
        this.month = gM
    }
}