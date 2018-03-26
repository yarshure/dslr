package com.remoteyourcam.usb.ptp;

import com.facebook.imageutils.JfifUtil;
import com.facebook.soloader.MinElf;
import com.igexin.assist.sdk.AssistPushConsts;
import com.igexin.download.Downloads;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.raizlabs.android.dbflow.sql.language.Condition.Operation;
import com.remoteyourcam.usb.R;
import com.remoteyourcam.usb.ptp.PtpConstants.Product;
import com.remoteyourcam.usb.ptp.PtpConstants.Property;
import com.ut.device.AidConstants;
import java.util.HashMap;
import java.util.Map;

public class PtpPropertyHelper {
    public static final int EOS_SHUTTER_SPEED_BULB = 12;
    private static final Map<Integer, String> eosApertureValueMap = new HashMap();
    private static final Map<Integer, Integer> eosDriveModeIconsMap = new HashMap();
    private static final Map<Integer, String> eosDriveModeMap = new HashMap();
    private static final Map<Integer, String> eosFocusModeMap = new HashMap();
    private static final Map<Integer, String> eosIsoSpeedMap = new HashMap();
    private static final Map<Integer, Integer> eosMeteringModeIconsMap = new HashMap();
    private static final Map<Integer, String> eosPictureStyleMap = new HashMap();
    private static final Map<Integer, Integer> eosShootingModeIconsMap = new HashMap();
    private static final Map<Integer, String> eosShootingModeMap = new HashMap();
    private static final Map<Integer, String> eosShutterSpeedMap = new HashMap();
    private static final Map<Integer, Integer> eosWhitebalanceIconsMap = new HashMap();
    private static final Map<Integer, String> eosWhitebalanceMap = new HashMap();
    private static final Map<Integer, String> nikonActivePicCtrlItemMap = new HashMap();
    private static final Map<Integer, String> nikonExposureIndexMap = new HashMap();
    private static final Map<Integer, Integer> nikonExposureProgramMap = new HashMap();
    private static final Map<Integer, Integer> nikonFocusMeteringModeIconsMap = new HashMap();
    private static final Map<Integer, String> nikonFocusMeteringModeMap = new HashMap();
    private static final Map<Integer, String> nikonFocusModeMap = new HashMap();
    private static final Map<Integer, Integer> nikonMeteringModeMap = new HashMap();
    private static final Map<Integer, String> nikonWbColorTempD200Map = new HashMap();
    private static final Map<Integer, String> nikonWbColorTempD300SMap = new HashMap();
    private static final Map<Integer, Integer> nikonWhitebalanceIconsMap = new HashMap();
    private static final Map<Integer, String> nikonWhitebalanceMap = new HashMap();

    static {
        eosShutterSpeedMap.put(Integer.valueOf(12), "Bulb");
        eosShutterSpeedMap.put(Integer.valueOf(16), "30\"");
        eosShutterSpeedMap.put(Integer.valueOf(19), "25\"");
        eosShutterSpeedMap.put(Integer.valueOf(20), "20\"");
        eosShutterSpeedMap.put(Integer.valueOf(21), "20\"");
        eosShutterSpeedMap.put(Integer.valueOf(24), "15\"");
        eosShutterSpeedMap.put(Integer.valueOf(27), "13\"");
        eosShutterSpeedMap.put(Integer.valueOf(28), "10\"");
        eosShutterSpeedMap.put(Integer.valueOf(29), "10\"");
        eosShutterSpeedMap.put(Integer.valueOf(32), "8\"");
        eosShutterSpeedMap.put(Integer.valueOf(35), "6\"");
        eosShutterSpeedMap.put(Integer.valueOf(36), "6\"");
        eosShutterSpeedMap.put(Integer.valueOf(37), "5\"");
        eosShutterSpeedMap.put(Integer.valueOf(40), "4\"");
        eosShutterSpeedMap.put(Integer.valueOf(43), "3\"2");
        eosShutterSpeedMap.put(Integer.valueOf(44), "3\"");
        eosShutterSpeedMap.put(Integer.valueOf(45), "2\"5");
        eosShutterSpeedMap.put(Integer.valueOf(37), "5\"");
        eosShutterSpeedMap.put(Integer.valueOf(40), "4\"");
        eosShutterSpeedMap.put(Integer.valueOf(43), "3\"2");
        eosShutterSpeedMap.put(Integer.valueOf(44), "3\"");
        eosShutterSpeedMap.put(Integer.valueOf(45), "2\"5");
        eosShutterSpeedMap.put(Integer.valueOf(48), "2\"");
        eosShutterSpeedMap.put(Integer.valueOf(51), "1\"6");
        eosShutterSpeedMap.put(Integer.valueOf(52), "1\"5");
        eosShutterSpeedMap.put(Integer.valueOf(53), "1\"3");
        eosShutterSpeedMap.put(Integer.valueOf(56), "1");
        eosShutterSpeedMap.put(Integer.valueOf(59), "0\"8");
        eosShutterSpeedMap.put(Integer.valueOf(60), "0\"7");
        eosShutterSpeedMap.put(Integer.valueOf(61), "0\"6");
        eosShutterSpeedMap.put(Integer.valueOf(64), "0\"5");
        eosShutterSpeedMap.put(Integer.valueOf(67), "0\"4");
        eosShutterSpeedMap.put(Integer.valueOf(68), "0\"3");
        eosShutterSpeedMap.put(Integer.valueOf(69), "0\"3");
        eosShutterSpeedMap.put(Integer.valueOf(72), "1/4");
        eosShutterSpeedMap.put(Integer.valueOf(75), "1/5");
        eosShutterSpeedMap.put(Integer.valueOf(76), "1/6");
        eosShutterSpeedMap.put(Integer.valueOf(77), "1/6");
        eosShutterSpeedMap.put(Integer.valueOf(80), "1/8");
        eosShutterSpeedMap.put(Integer.valueOf(83), "1/10");
        eosShutterSpeedMap.put(Integer.valueOf(84), "1/10");
        eosShutterSpeedMap.put(Integer.valueOf(85), "1/13");
        eosShutterSpeedMap.put(Integer.valueOf(88), "1/15");
        eosShutterSpeedMap.put(Integer.valueOf(91), "1/20");
        eosShutterSpeedMap.put(Integer.valueOf(92), "1/20");
        eosShutterSpeedMap.put(Integer.valueOf(93), "1/25");
        eosShutterSpeedMap.put(Integer.valueOf(96), "1/30");
        eosShutterSpeedMap.put(Integer.valueOf(99), "1/40");
        eosShutterSpeedMap.put(Integer.valueOf(100), "1/45");
        eosShutterSpeedMap.put(Integer.valueOf(101), "1/50");
        eosShutterSpeedMap.put(Integer.valueOf(104), "1/60");
        eosShutterSpeedMap.put(Integer.valueOf(107), "1/80");
        eosShutterSpeedMap.put(Integer.valueOf(108), "1/90");
        eosShutterSpeedMap.put(Integer.valueOf(109), "1/100");
        eosShutterSpeedMap.put(Integer.valueOf(112), "1/125");
        eosShutterSpeedMap.put(Integer.valueOf(115), "1/160");
        eosShutterSpeedMap.put(Integer.valueOf(116), "1/180");
        eosShutterSpeedMap.put(Integer.valueOf(117), "1/200");
        eosShutterSpeedMap.put(Integer.valueOf(120), "1/250");
        eosShutterSpeedMap.put(Integer.valueOf(123), "1/320");
        eosShutterSpeedMap.put(Integer.valueOf(124), "1/350");
        eosShutterSpeedMap.put(Integer.valueOf(125), "1/400");
        eosShutterSpeedMap.put(Integer.valueOf(128), "1/500");
        eosShutterSpeedMap.put(Integer.valueOf(131), "1/640");
        eosShutterSpeedMap.put(Integer.valueOf(132), "1/750");
        eosShutterSpeedMap.put(Integer.valueOf(133), "1/800");
        eosShutterSpeedMap.put(Integer.valueOf(136), "1/1000");
        eosShutterSpeedMap.put(Integer.valueOf(139), "1/1250");
        eosShutterSpeedMap.put(Integer.valueOf(140), "1/1500");
        eosShutterSpeedMap.put(Integer.valueOf(141), "1/1600");
        eosShutterSpeedMap.put(Integer.valueOf(144), "1/2000");
        eosShutterSpeedMap.put(Integer.valueOf(147), "1/2500");
        eosShutterSpeedMap.put(Integer.valueOf(148), "1/3000");
        eosShutterSpeedMap.put(Integer.valueOf(149), "1/3200");
        eosShutterSpeedMap.put(Integer.valueOf(152), "1/4000");
        eosShutterSpeedMap.put(Integer.valueOf(155), "1/5000");
        eosShutterSpeedMap.put(Integer.valueOf(156), "1/6000");
        eosShutterSpeedMap.put(Integer.valueOf(157), "1/6400");
        eosShutterSpeedMap.put(Integer.valueOf(160), "1/8000");
        eosApertureValueMap.put(Integer.valueOf(8), "1");
        eosApertureValueMap.put(Integer.valueOf(11), "1.1");
        eosApertureValueMap.put(Integer.valueOf(12), "1.2");
        eosApertureValueMap.put(Integer.valueOf(13), "1.2");
        eosApertureValueMap.put(Integer.valueOf(16), "1.4");
        eosApertureValueMap.put(Integer.valueOf(19), "1.6");
        eosApertureValueMap.put(Integer.valueOf(20), "1.8");
        eosApertureValueMap.put(Integer.valueOf(21), "1.8");
        eosApertureValueMap.put(Integer.valueOf(24), "2");
        eosApertureValueMap.put(Integer.valueOf(27), "2.2");
        eosApertureValueMap.put(Integer.valueOf(28), "2.5");
        eosApertureValueMap.put(Integer.valueOf(29), "2.5");
        eosApertureValueMap.put(Integer.valueOf(32), "2.8");
        eosApertureValueMap.put(Integer.valueOf(35), "3.2");
        eosApertureValueMap.put(Integer.valueOf(36), "3.5");
        eosApertureValueMap.put(Integer.valueOf(37), "3.5");
        eosApertureValueMap.put(Integer.valueOf(40), "4");
        eosApertureValueMap.put(Integer.valueOf(43), "4.5");
        eosApertureValueMap.put(Integer.valueOf(44), "4.5");
        eosApertureValueMap.put(Integer.valueOf(45), "5.0");
        eosApertureValueMap.put(Integer.valueOf(48), "5.6");
        eosApertureValueMap.put(Integer.valueOf(51), "6.3");
        eosApertureValueMap.put(Integer.valueOf(52), "6.7");
        eosApertureValueMap.put(Integer.valueOf(53), "7.1");
        eosApertureValueMap.put(Integer.valueOf(56), "8");
        eosApertureValueMap.put(Integer.valueOf(59), "9");
        eosApertureValueMap.put(Integer.valueOf(60), "9.5");
        eosApertureValueMap.put(Integer.valueOf(61), "10");
        eosApertureValueMap.put(Integer.valueOf(64), "11");
        eosApertureValueMap.put(Integer.valueOf(67), "13");
        eosApertureValueMap.put(Integer.valueOf(68), "13");
        eosApertureValueMap.put(Integer.valueOf(69), "14");
        eosApertureValueMap.put(Integer.valueOf(72), "16");
        eosApertureValueMap.put(Integer.valueOf(75), "18");
        eosApertureValueMap.put(Integer.valueOf(76), "19");
        eosApertureValueMap.put(Integer.valueOf(77), "20");
        eosApertureValueMap.put(Integer.valueOf(80), "22");
        eosApertureValueMap.put(Integer.valueOf(83), "25");
        eosApertureValueMap.put(Integer.valueOf(84), "27");
        eosApertureValueMap.put(Integer.valueOf(85), "29");
        eosApertureValueMap.put(Integer.valueOf(88), "32");
        eosApertureValueMap.put(Integer.valueOf(91), "36");
        eosApertureValueMap.put(Integer.valueOf(92), "38");
        eosApertureValueMap.put(Integer.valueOf(93), "40");
        eosApertureValueMap.put(Integer.valueOf(96), "45");
        eosApertureValueMap.put(Integer.valueOf(99), "51");
        eosApertureValueMap.put(Integer.valueOf(100), "54");
        eosApertureValueMap.put(Integer.valueOf(101), "57");
        eosApertureValueMap.put(Integer.valueOf(104), "64");
        eosApertureValueMap.put(Integer.valueOf(107), "72");
        eosApertureValueMap.put(Integer.valueOf(108), "76");
        eosApertureValueMap.put(Integer.valueOf(109), "80");
        eosApertureValueMap.put(Integer.valueOf(112), "91");
        eosIsoSpeedMap.put(Integer.valueOf(0), "Auto");
        eosIsoSpeedMap.put(Integer.valueOf(40), "6");
        eosIsoSpeedMap.put(Integer.valueOf(48), "12");
        eosIsoSpeedMap.put(Integer.valueOf(56), "25");
        eosIsoSpeedMap.put(Integer.valueOf(64), "50");
        eosIsoSpeedMap.put(Integer.valueOf(72), "100");
        eosIsoSpeedMap.put(Integer.valueOf(75), "125");
        eosIsoSpeedMap.put(Integer.valueOf(77), "160");
        eosIsoSpeedMap.put(Integer.valueOf(80), "200");
        eosIsoSpeedMap.put(Integer.valueOf(83), "250");
        eosIsoSpeedMap.put(Integer.valueOf(85), "320");
        eosIsoSpeedMap.put(Integer.valueOf(88), "400");
        eosIsoSpeedMap.put(Integer.valueOf(91), "500");
        eosIsoSpeedMap.put(Integer.valueOf(93), "640");
        eosIsoSpeedMap.put(Integer.valueOf(96), "800");
        eosIsoSpeedMap.put(Integer.valueOf(99), "1000");
        eosIsoSpeedMap.put(Integer.valueOf(101), "1250");
        eosIsoSpeedMap.put(Integer.valueOf(104), "1600");
        eosIsoSpeedMap.put(Integer.valueOf(107), "2000");
        eosIsoSpeedMap.put(Integer.valueOf(109), "2500");
        eosIsoSpeedMap.put(Integer.valueOf(112), "3200");
        eosIsoSpeedMap.put(Integer.valueOf(115), "4000");
        eosIsoSpeedMap.put(Integer.valueOf(117), "5000");
        eosIsoSpeedMap.put(Integer.valueOf(120), "6400");
        eosIsoSpeedMap.put(Integer.valueOf(128), "12800");
        eosIsoSpeedMap.put(Integer.valueOf(136), "25600");
        eosIsoSpeedMap.put(Integer.valueOf(144), "51200");
        eosIsoSpeedMap.put(Integer.valueOf(152), "102400");
        eosWhitebalanceMap.put(Integer.valueOf(0), "Auto");
        eosWhitebalanceMap.put(Integer.valueOf(1), "Daylight");
        eosWhitebalanceMap.put(Integer.valueOf(2), "Cloudy");
        eosWhitebalanceMap.put(Integer.valueOf(3), "Tungsten");
        eosWhitebalanceMap.put(Integer.valueOf(4), "Fluorescent");
        eosWhitebalanceMap.put(Integer.valueOf(5), "Flash");
        eosWhitebalanceMap.put(Integer.valueOf(6), "Manual 1");
        eosWhitebalanceMap.put(Integer.valueOf(8), "Shade");
        eosWhitebalanceMap.put(Integer.valueOf(9), "Color temperature");
        eosWhitebalanceMap.put(Integer.valueOf(10), "PC-1");
        eosWhitebalanceMap.put(Integer.valueOf(11), "PC-2");
        eosWhitebalanceMap.put(Integer.valueOf(12), "PC-3");
        eosWhitebalanceMap.put(Integer.valueOf(15), "Manual 2");
        eosWhitebalanceMap.put(Integer.valueOf(16), "Manual 3");
        eosWhitebalanceMap.put(Integer.valueOf(18), "Manual 4");
        eosWhitebalanceMap.put(Integer.valueOf(19), "Manual");
        eosWhitebalanceMap.put(Integer.valueOf(20), "PC-4");
        eosWhitebalanceMap.put(Integer.valueOf(21), "PC-5");
        eosWhitebalanceIconsMap.put(Integer.valueOf(0), Integer.valueOf(R.drawable.whitebalance_auto));
        eosWhitebalanceIconsMap.put(Integer.valueOf(1), Integer.valueOf(R.drawable.whitebalance_daylight));
        eosWhitebalanceIconsMap.put(Integer.valueOf(2), Integer.valueOf(R.drawable.whitebalance_cloudy));
        eosWhitebalanceIconsMap.put(Integer.valueOf(3), Integer.valueOf(R.drawable.whitebalance_tungsten));
        eosWhitebalanceIconsMap.put(Integer.valueOf(4), Integer.valueOf(R.drawable.whitebalance_fluorescent));
        eosWhitebalanceIconsMap.put(Integer.valueOf(5), Integer.valueOf(R.drawable.whitebalance_flash));
        eosWhitebalanceIconsMap.put(Integer.valueOf(6), Integer.valueOf(R.drawable.whitebalance_manual1));
        eosWhitebalanceIconsMap.put(Integer.valueOf(8), Integer.valueOf(R.drawable.whitebalance_shade));
        eosWhitebalanceIconsMap.put(Integer.valueOf(9), Integer.valueOf(R.drawable.whitebalance_color_temperature));
        eosWhitebalanceIconsMap.put(Integer.valueOf(10), Integer.valueOf(R.drawable.whitebalance_custom1));
        eosWhitebalanceIconsMap.put(Integer.valueOf(11), Integer.valueOf(R.drawable.whitebalance_custom2));
        eosWhitebalanceIconsMap.put(Integer.valueOf(12), Integer.valueOf(R.drawable.whitebalance_custom3));
        eosWhitebalanceIconsMap.put(Integer.valueOf(15), Integer.valueOf(R.drawable.whitebalance_manual2));
        eosWhitebalanceIconsMap.put(Integer.valueOf(16), Integer.valueOf(R.drawable.whitebalance_manual3));
        eosWhitebalanceIconsMap.put(Integer.valueOf(18), Integer.valueOf(R.drawable.whitebalance_manual4));
        eosWhitebalanceIconsMap.put(Integer.valueOf(19), Integer.valueOf(R.drawable.whitebalance_manual5));
        eosWhitebalanceIconsMap.put(Integer.valueOf(20), Integer.valueOf(R.drawable.whitebalance_custom4));
        eosWhitebalanceIconsMap.put(Integer.valueOf(21), Integer.valueOf(R.drawable.whitebalance_custom5));
        eosShootingModeMap.put(Integer.valueOf(0), "Program AE");
        eosShootingModeMap.put(Integer.valueOf(1), "Shutter-Speed Priority AE");
        eosShootingModeMap.put(Integer.valueOf(2), "Aperture Priority AE");
        eosShootingModeMap.put(Integer.valueOf(3), "Manual Exposure");
        eosShootingModeMap.put(Integer.valueOf(4), "Bulb");
        eosShootingModeMap.put(Integer.valueOf(5), "Auto Depth-of-Field AE");
        eosShootingModeMap.put(Integer.valueOf(6), "Depth-of-Field AE");
        eosShootingModeMap.put(Integer.valueOf(8), "Lock");
        eosShootingModeMap.put(Integer.valueOf(9), "Auto");
        eosShootingModeMap.put(Integer.valueOf(10), "Night Scene Portrait");
        eosShootingModeMap.put(Integer.valueOf(11), "Sports");
        eosShootingModeMap.put(Integer.valueOf(12), "Portrait");
        eosShootingModeMap.put(Integer.valueOf(13), "Landscape");
        eosShootingModeMap.put(Integer.valueOf(14), "Close-Up");
        eosShootingModeMap.put(Integer.valueOf(15), "Flash Off");
        eosShootingModeMap.put(Integer.valueOf(19), "Creative Auto");
        eosShootingModeIconsMap.put(Integer.valueOf(0), Integer.valueOf(R.drawable.shootingmode_program));
        eosShootingModeIconsMap.put(Integer.valueOf(1), Integer.valueOf(R.drawable.shootingmode_tv));
        eosShootingModeIconsMap.put(Integer.valueOf(2), Integer.valueOf(R.drawable.shootingmode_av));
        eosShootingModeIconsMap.put(Integer.valueOf(3), Integer.valueOf(R.drawable.shootingmode_m));
        eosShootingModeIconsMap.put(Integer.valueOf(4), Integer.valueOf(R.drawable.shootingmode_bulb));
        eosShootingModeIconsMap.put(Integer.valueOf(5), Integer.valueOf(R.drawable.shootingmode_adep));
        eosShootingModeIconsMap.put(Integer.valueOf(6), Integer.valueOf(R.drawable.shootingmode_dep));
        eosShootingModeIconsMap.put(Integer.valueOf(8), Integer.valueOf(R.drawable.shootingmode_lock));
        eosShootingModeIconsMap.put(Integer.valueOf(9), Integer.valueOf(R.drawable.shootingmode_auto));
        eosShootingModeIconsMap.put(Integer.valueOf(10), Integer.valueOf(R.drawable.shootingmode_night_scene_portrait));
        eosShootingModeIconsMap.put(Integer.valueOf(11), Integer.valueOf(R.drawable.shootingmode_sports));
        eosShootingModeIconsMap.put(Integer.valueOf(12), Integer.valueOf(R.drawable.shootingmode_portrait));
        eosShootingModeIconsMap.put(Integer.valueOf(13), Integer.valueOf(R.drawable.shootingmode_landscape));
        eosShootingModeIconsMap.put(Integer.valueOf(14), Integer.valueOf(R.drawable.shootingmode_close_up));
        eosShootingModeIconsMap.put(Integer.valueOf(15), Integer.valueOf(R.drawable.shootingmode_flash_off));
        eosShootingModeIconsMap.put(Integer.valueOf(19), Integer.valueOf(R.drawable.shootingmode_creativeauto));
        eosDriveModeMap.put(Integer.valueOf(0), "Single Shooting");
        eosDriveModeMap.put(Integer.valueOf(1), "Continuous Shooting");
        eosDriveModeMap.put(Integer.valueOf(2), "Video");
        eosDriveModeMap.put(Integer.valueOf(3), Operation.EMPTY_PARAM);
        eosDriveModeMap.put(Integer.valueOf(4), "High-Speed Continuous Shooting");
        eosDriveModeMap.put(Integer.valueOf(5), "Low-Speed Continuous Shooting");
        eosDriveModeMap.put(Integer.valueOf(6), "Silent Single Shooting");
        eosDriveModeMap.put(Integer.valueOf(7), "10-Sec Self-Timer plus Continuous Shooting");
        eosDriveModeMap.put(Integer.valueOf(16), "10-Sec Self-Timer");
        eosDriveModeMap.put(Integer.valueOf(17), "2-Sec Self-Timer");
        eosFocusModeMap.put(Integer.valueOf(0), "One-Shot AF");
        eosFocusModeMap.put(Integer.valueOf(1), "AI Servo AF");
        eosFocusModeMap.put(Integer.valueOf(2), "AI Focus AF");
        eosFocusModeMap.put(Integer.valueOf(3), "Manual Focus");
        nikonWhitebalanceMap.put(Integer.valueOf(2), "Auto");
        nikonWhitebalanceMap.put(Integer.valueOf(4), "Sunny");
        nikonWhitebalanceMap.put(Integer.valueOf(5), "Fluorescent");
        nikonWhitebalanceMap.put(Integer.valueOf(6), "Incandescent");
        nikonWhitebalanceMap.put(Integer.valueOf(7), "Flash");
        nikonWhitebalanceMap.put(Integer.valueOf(32784), "Cloudy");
        nikonWhitebalanceMap.put(Integer.valueOf(32785), "Sunny shade");
        nikonWhitebalanceMap.put(Integer.valueOf(32786), "Color temperature");
        nikonWhitebalanceMap.put(Integer.valueOf(32787), "Preset");
        nikonWhitebalanceIconsMap.put(Integer.valueOf(2), Integer.valueOf(R.drawable.whitebalance_auto));
        nikonWhitebalanceIconsMap.put(Integer.valueOf(4), Integer.valueOf(R.drawable.whitebalance_daylight));
        nikonWhitebalanceIconsMap.put(Integer.valueOf(5), Integer.valueOf(R.drawable.whitebalance_fluorescent));
        nikonWhitebalanceIconsMap.put(Integer.valueOf(6), Integer.valueOf(R.drawable.whitebalance_tungsten));
        nikonWhitebalanceIconsMap.put(Integer.valueOf(7), Integer.valueOf(R.drawable.whitebalance_flash));
        nikonWhitebalanceIconsMap.put(Integer.valueOf(32784), Integer.valueOf(R.drawable.whitebalance_cloudy));
        nikonWhitebalanceIconsMap.put(Integer.valueOf(32785), Integer.valueOf(R.drawable.whitebalance_shade));
        nikonWhitebalanceIconsMap.put(Integer.valueOf(32786), Integer.valueOf(R.drawable.whitebalance_color_temperature));
        nikonWhitebalanceIconsMap.put(Integer.valueOf(32787), Integer.valueOf(R.drawable.whitebalance_custom1));
        nikonExposureIndexMap.put(Integer.valueOf(100), "100");
        nikonExposureIndexMap.put(Integer.valueOf(125), "125");
        nikonExposureIndexMap.put(Integer.valueOf(160), "160");
        nikonExposureIndexMap.put(Integer.valueOf(Downloads.STATUS_SUCCESS), "200");
        nikonExposureIndexMap.put(Integer.valueOf(250), "250");
        nikonExposureIndexMap.put(Integer.valueOf(280), "280");
        nikonExposureIndexMap.put(Integer.valueOf(320), "320");
        nikonExposureIndexMap.put(Integer.valueOf(Downloads.STATUS_BAD_REQUEST), "400");
        nikonExposureIndexMap.put(Integer.valueOf(500), "500");
        nikonExposureIndexMap.put(Integer.valueOf(560), "560");
        nikonExposureIndexMap.put(Integer.valueOf(640), "640");
        nikonExposureIndexMap.put(Integer.valueOf(800), "800");
        nikonExposureIndexMap.put(Integer.valueOf(AidConstants.EVENT_REQUEST_STARTED), "1000");
        nikonExposureIndexMap.put(Integer.valueOf(1100), "1100");
        nikonExposureIndexMap.put(Integer.valueOf(1250), "1250");
        nikonExposureIndexMap.put(Integer.valueOf(1600), "1600");
        nikonExposureIndexMap.put(Integer.valueOf(2000), "2000");
        nikonExposureIndexMap.put(Integer.valueOf(2200), "2200");
        nikonExposureIndexMap.put(Integer.valueOf(2500), "2500");
        nikonExposureIndexMap.put(Integer.valueOf(3200), "3200");
        nikonExposureIndexMap.put(Integer.valueOf(4000), "4000");
        nikonExposureIndexMap.put(Integer.valueOf(4500), "4500");
        nikonExposureIndexMap.put(Integer.valueOf(BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT), "5000");
        nikonExposureIndexMap.put(Integer.valueOf(6400), "6400");
        nikonExposureIndexMap.put(Integer.valueOf(8000), "8000");
        nikonExposureIndexMap.put(Integer.valueOf(9000), "9000");
        nikonExposureIndexMap.put(Integer.valueOf(10000), "10000");
        nikonExposureIndexMap.put(Integer.valueOf(12800), "12800");
        nikonExposureProgramMap.put(Integer.valueOf(1), Integer.valueOf(R.drawable.shootingmode_m));
        nikonExposureProgramMap.put(Integer.valueOf(2), Integer.valueOf(R.drawable.shootingmode_program));
        nikonExposureProgramMap.put(Integer.valueOf(3), Integer.valueOf(R.drawable.shootingmode_av));
        nikonExposureProgramMap.put(Integer.valueOf(4), Integer.valueOf(R.drawable.shootingmode_tv));
        nikonExposureProgramMap.put(Integer.valueOf(32784), Integer.valueOf(R.drawable.shootingmode_auto));
        nikonExposureProgramMap.put(Integer.valueOf(32785), Integer.valueOf(R.drawable.shootingmode_portrait));
        nikonExposureProgramMap.put(Integer.valueOf(32786), Integer.valueOf(R.drawable.shootingmode_landscape));
        nikonExposureProgramMap.put(Integer.valueOf(32787), Integer.valueOf(R.drawable.shootingmode_close_up));
        nikonExposureProgramMap.put(Integer.valueOf(32788), Integer.valueOf(R.drawable.shootingmode_sports));
        nikonExposureProgramMap.put(Integer.valueOf(32789), Integer.valueOf(R.drawable.shootingmode_night_scene_portrait));
        nikonExposureProgramMap.put(Integer.valueOf(32790), Integer.valueOf(R.drawable.shootingmode_flash_off));
        nikonExposureProgramMap.put(Integer.valueOf(32791), Integer.valueOf(R.drawable.shootingmode_unknown));
        nikonExposureProgramMap.put(Integer.valueOf(32792), Integer.valueOf(R.drawable.shootingmode_unknown));
        nikonExposureProgramMap.put(Integer.valueOf(32848), Integer.valueOf(R.drawable.shootingmode_unknown));
        nikonExposureProgramMap.put(Integer.valueOf(32849), Integer.valueOf(R.drawable.shootingmode_unknown));
        nikonWbColorTempD300SMap.put(Integer.valueOf(0), "2500K");
        nikonWbColorTempD300SMap.put(Integer.valueOf(1), "2560K");
        nikonWbColorTempD300SMap.put(Integer.valueOf(2), "2630K");
        nikonWbColorTempD300SMap.put(Integer.valueOf(3), "2700K");
        nikonWbColorTempD300SMap.put(Integer.valueOf(4), "2780K");
        nikonWbColorTempD300SMap.put(Integer.valueOf(5), "2860K");
        nikonWbColorTempD300SMap.put(Integer.valueOf(6), "2940K");
        nikonWbColorTempD300SMap.put(Integer.valueOf(7), "3030K");
        nikonWbColorTempD300SMap.put(Integer.valueOf(8), "3130K");
        nikonWbColorTempD300SMap.put(Integer.valueOf(9), "3230K");
        nikonWbColorTempD300SMap.put(Integer.valueOf(10), "3330K");
        nikonWbColorTempD300SMap.put(Integer.valueOf(11), "3450K");
        nikonWbColorTempD300SMap.put(Integer.valueOf(12), "3570K");
        nikonWbColorTempD300SMap.put(Integer.valueOf(13), "3700K");
        nikonWbColorTempD300SMap.put(Integer.valueOf(14), "3850K");
        nikonWbColorTempD300SMap.put(Integer.valueOf(15), "4000K");
        nikonWbColorTempD300SMap.put(Integer.valueOf(16), "4170K");
        nikonWbColorTempD300SMap.put(Integer.valueOf(17), "4350K");
        nikonWbColorTempD300SMap.put(Integer.valueOf(18), "4550K");
        nikonWbColorTempD300SMap.put(Integer.valueOf(19), "4760K");
        nikonWbColorTempD300SMap.put(Integer.valueOf(20), "5000K");
        nikonWbColorTempD300SMap.put(Integer.valueOf(21), "5260K");
        nikonWbColorTempD300SMap.put(Integer.valueOf(22), "5560K");
        nikonWbColorTempD300SMap.put(Integer.valueOf(23), "5880K");
        nikonWbColorTempD300SMap.put(Integer.valueOf(24), "6250K");
        nikonWbColorTempD300SMap.put(Integer.valueOf(25), "6670K");
        nikonWbColorTempD300SMap.put(Integer.valueOf(26), "7140K");
        nikonWbColorTempD300SMap.put(Integer.valueOf(27), "7690K");
        nikonWbColorTempD300SMap.put(Integer.valueOf(28), "8330K");
        nikonWbColorTempD300SMap.put(Integer.valueOf(29), "9090K");
        nikonWbColorTempD300SMap.put(Integer.valueOf(30), "10000K");
        nikonWbColorTempD200Map.put(Integer.valueOf(0), "2500K");
        nikonWbColorTempD200Map.put(Integer.valueOf(1), "2550K");
        nikonWbColorTempD200Map.put(Integer.valueOf(2), "2650K");
        nikonWbColorTempD200Map.put(Integer.valueOf(3), "2700K");
        nikonWbColorTempD200Map.put(Integer.valueOf(4), "2800K");
        nikonWbColorTempD200Map.put(Integer.valueOf(5), "2850K");
        nikonWbColorTempD200Map.put(Integer.valueOf(6), "2950K");
        nikonWbColorTempD200Map.put(Integer.valueOf(7), "3000K");
        nikonWbColorTempD200Map.put(Integer.valueOf(8), "3100K");
        nikonWbColorTempD200Map.put(Integer.valueOf(9), "3200K");
        nikonWbColorTempD200Map.put(Integer.valueOf(10), "3300K");
        nikonWbColorTempD200Map.put(Integer.valueOf(11), "3400K");
        nikonWbColorTempD200Map.put(Integer.valueOf(12), "3600K");
        nikonWbColorTempD200Map.put(Integer.valueOf(13), "3700K");
        nikonWbColorTempD200Map.put(Integer.valueOf(14), "3800K");
        nikonWbColorTempD200Map.put(Integer.valueOf(15), "4000K");
        nikonWbColorTempD200Map.put(Integer.valueOf(16), "4200K");
        nikonWbColorTempD200Map.put(Integer.valueOf(17), "4300K");
        nikonWbColorTempD200Map.put(Integer.valueOf(18), "4500K");
        nikonWbColorTempD200Map.put(Integer.valueOf(19), "4800K");
        nikonWbColorTempD200Map.put(Integer.valueOf(20), "5000K");
        nikonWbColorTempD200Map.put(Integer.valueOf(21), "5300K");
        nikonWbColorTempD200Map.put(Integer.valueOf(22), "5600K");
        nikonWbColorTempD200Map.put(Integer.valueOf(23), "5900K");
        nikonWbColorTempD200Map.put(Integer.valueOf(24), "6300K");
        nikonWbColorTempD200Map.put(Integer.valueOf(25), "6700K");
        nikonWbColorTempD200Map.put(Integer.valueOf(26), "7100K");
        nikonWbColorTempD200Map.put(Integer.valueOf(27), "7700K");
        nikonWbColorTempD200Map.put(Integer.valueOf(28), "8300K");
        nikonWbColorTempD200Map.put(Integer.valueOf(29), "9100K");
        nikonWbColorTempD200Map.put(Integer.valueOf(30), "10000K");
        nikonFocusModeMap.put(Integer.valueOf(1), "Manual Focus");
        nikonFocusModeMap.put(Integer.valueOf(32784), "Single AF servo");
        nikonFocusModeMap.put(Integer.valueOf(32785), "Continous AF servo");
        nikonFocusModeMap.put(Integer.valueOf(32786), "AF servo auto switch");
        nikonFocusModeMap.put(Integer.valueOf(32787), "Constant AF servo");
        nikonActivePicCtrlItemMap.put(Integer.valueOf(1), "SD");
        nikonActivePicCtrlItemMap.put(Integer.valueOf(2), "NL");
        nikonActivePicCtrlItemMap.put(Integer.valueOf(3), "VI");
        nikonActivePicCtrlItemMap.put(Integer.valueOf(4), "MC");
        nikonActivePicCtrlItemMap.put(Integer.valueOf(5), AssistPushConsts.MSG_VALUE_PAYLOAD);
        nikonActivePicCtrlItemMap.put(Integer.valueOf(6), "LS");
        nikonActivePicCtrlItemMap.put(Integer.valueOf(101), "O-1");
        nikonActivePicCtrlItemMap.put(Integer.valueOf(102), "O-2");
        nikonActivePicCtrlItemMap.put(Integer.valueOf(103), "O-3");
        nikonActivePicCtrlItemMap.put(Integer.valueOf(104), "O-4");
        nikonActivePicCtrlItemMap.put(Integer.valueOf(201), "C-1");
        nikonActivePicCtrlItemMap.put(Integer.valueOf(202), "C-2");
        nikonActivePicCtrlItemMap.put(Integer.valueOf(203), "C-3");
        nikonActivePicCtrlItemMap.put(Integer.valueOf(204), "C-4");
        nikonActivePicCtrlItemMap.put(Integer.valueOf(205), "C-5");
        nikonActivePicCtrlItemMap.put(Integer.valueOf(206), "C-6");
        nikonActivePicCtrlItemMap.put(Integer.valueOf(207), "C-7");
        nikonActivePicCtrlItemMap.put(Integer.valueOf(JfifUtil.MARKER_RST0), "C-8");
        nikonActivePicCtrlItemMap.put(Integer.valueOf(209), "C-9");
        nikonMeteringModeMap.put(Integer.valueOf(2), Integer.valueOf(R.drawable.metering_exposure_center_weighted_nikon));
        nikonMeteringModeMap.put(Integer.valueOf(3), Integer.valueOf(R.drawable.metering_exposure_matrix_nikon));
        nikonMeteringModeMap.put(Integer.valueOf(4), Integer.valueOf(R.drawable.metering_exposure_spot));
        eosMeteringModeIconsMap.put(Integer.valueOf(1), Integer.valueOf(R.drawable.metering_exposure_spot));
        eosMeteringModeIconsMap.put(Integer.valueOf(3), Integer.valueOf(R.drawable.metering_exposure_evaluative_canon));
        eosMeteringModeIconsMap.put(Integer.valueOf(4), Integer.valueOf(R.drawable.metering_exposure_partial));
        eosMeteringModeIconsMap.put(Integer.valueOf(5), Integer.valueOf(R.drawable.metering_exposure_center_weighted_average_canon));
        nikonFocusMeteringModeMap.put(Integer.valueOf(2), "Dynamic");
        nikonFocusMeteringModeMap.put(Integer.valueOf(32784), "Single point");
        nikonFocusMeteringModeMap.put(Integer.valueOf(32785), "Auto area");
        nikonFocusMeteringModeMap.put(Integer.valueOf(32786), "3D");
        nikonFocusMeteringModeIconsMap.put(Integer.valueOf(2), Integer.valueOf(R.drawable.metering_af_dynamic_area));
        nikonFocusMeteringModeIconsMap.put(Integer.valueOf(32784), Integer.valueOf(R.drawable.metering_af_single_point));
        nikonFocusMeteringModeIconsMap.put(Integer.valueOf(32785), Integer.valueOf(R.drawable.metering_af_auto_area));
        nikonFocusMeteringModeIconsMap.put(Integer.valueOf(32786), Integer.valueOf(R.drawable.metering_af_3d_tracking));
        eosPictureStyleMap.put(Integer.valueOf(129), "ST");
        eosPictureStyleMap.put(Integer.valueOf(130), AssistPushConsts.MSG_VALUE_PAYLOAD);
        eosPictureStyleMap.put(Integer.valueOf(131), "LS");
        eosPictureStyleMap.put(Integer.valueOf(132), "NL");
        eosPictureStyleMap.put(Integer.valueOf(133), "FL");
        eosPictureStyleMap.put(Integer.valueOf(134), "MO");
        eosPictureStyleMap.put(Integer.valueOf(33), "UD1");
        eosPictureStyleMap.put(Integer.valueOf(34), "UD2");
        eosPictureStyleMap.put(Integer.valueOf(35), "UD3");
    }

    public static String getBiggestValue(int i) {
        switch (i) {
            case Property.FNumber /*20487*/:
                return "33.3";
            case Property.ExposureTime /*20493*/:
                return "1/10000";
            case Property.ExposureIndex /*20495*/:
                return "LO-0.3";
            case Property.EosApertureValue /*53505*/:
                return "f 9.5";
            case Property.EosShutterSpeed /*53506*/:
                return "1/8000";
            case Property.EosIsoSpeed /*53507*/:
                return "102400";
            default:
                return "";
        }
    }

    private static String getNikonExposureIndex(int i, int i2) {
        switch (i) {
            case Product.NikonD200 /*1040*/:
            case Product.NikonD80 /*1042*/:
            case Product.NikonD40 /*1044*/:
                if (i2 == 2000) {
                    return "Hi-0.3";
                }
                if (i2 == 2500) {
                    return "Hi-0.7";
                }
                if (i2 == 3200) {
                    return "Hi-1";
                }
                if (i2 == 2200) {
                    return "Hi-0.5";
                }
                break;
            case Product.NikonD300 /*1050*/:
            case Product.NikonD5000 /*1059*/:
            case Product.NikonD300S /*1061*/:
                if (i2 == 100) {
                    return "LO-1";
                }
                if (i2 == 125) {
                    return "LO-0.7";
                }
                if (i2 == 160) {
                    return "LO-0.3";
                }
                if (i2 == 4000) {
                    return "Hi-0.3";
                }
                if (i2 == 4500) {
                    return "Hi-0.5";
                }
                if (i2 == BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT) {
                    return "Hi-0.7";
                }
                if (i2 == 6400) {
                    return "Hi-1";
                }
                break;
            case Product.NikonD3 /*1052*/:
                if (i2 == 100) {
                    return "LO-1";
                }
                if (i2 == 125) {
                    return "LO-0.7";
                }
                if (i2 == 140) {
                    return "LO-0.5";
                }
                if (i2 == 160) {
                    return "LO-0.3";
                }
                if (i2 == 8320) {
                    return "Hi-0.3";
                }
                if (i2 == 8960) {
                    return "Hi-0.5";
                }
                if (i2 == 10240) {
                    return "Hi-0.7";
                }
                if (i2 == 12800) {
                    return "Hi-1";
                }
                if (i2 == 25600) {
                    return "Hi-2";
                }
                break;
            case Product.NikonD3X /*1056*/:
                if (i2 == 50) {
                    return "LO-1";
                }
                if (i2 == 62) {
                    return "LO-0.7";
                }
                if (i2 == 70) {
                    return "LO-0.5";
                }
                if (i2 == 80) {
                    return "LO-0.3";
                }
                if (i2 == 2000) {
                    return "Hi-0.3";
                }
                if (i2 == 2240) {
                    return "Hi-0.5";
                }
                if (i2 == 2560) {
                    return "Hi-0.7";
                }
                if (i2 == 3200) {
                    return "Hi-1";
                }
                if (i2 == 6400) {
                    return "Hi-2";
                }
                break;
            case Product.NikonD3S /*1062*/:
                if (i2 == 100) {
                    return "LO-1";
                }
                if (i2 == 125) {
                    return "LO-0.7";
                }
                if (i2 == 140) {
                    return "LO-0.5";
                }
                if (i2 == 160) {
                    return "LO-0.3";
                }
                if (i2 == 14400) {
                    return "Hi-0.3";
                }
                if (i2 == 18000) {
                    return "Hi-0.5";
                }
                if (i2 == BaseImageDownloader.DEFAULT_HTTP_READ_TIMEOUT) {
                    return "Hi-0.7";
                }
                if (i2 == 25600) {
                    return "Hi-1";
                }
                if (i2 == 51200) {
                    return "Hi-2";
                }
                break;
            case Product.NikonD7000 /*1064*/:
                if (i2 == 8000) {
                    return "Hi-0.3";
                }
                if (i2 == 9000) {
                    return "Hi-0.5";
                }
                if (i2 == 10000) {
                    return "Hi-0.7";
                }
                if (i2 == 12800) {
                    return "Hi-1";
                }
                if (i2 == 25600) {
                    return "Hi-2";
                }
                break;
        }
        return (String) nikonExposureIndexMap.get(Integer.valueOf(i2));
    }

    private static String getNikonWbColorTemp(int i, int i2) {
        switch (i) {
            case Product.NikonD200 /*1040*/:
            case Product.NikonD80 /*1042*/:
                return (String) nikonWbColorTempD200Map.get(Integer.valueOf(i2));
            case Product.NikonD300 /*1050*/:
            case Product.NikonD3 /*1052*/:
            case Product.NikonD3X /*1056*/:
            case Product.NikonD90 /*1057*/:
            case Product.NikonD700 /*1058*/:
            case Product.NikonD300S /*1061*/:
            case Product.NikonD3S /*1062*/:
            case Product.NikonD7000 /*1064*/:
                return (String) nikonWbColorTempD300SMap.get(Integer.valueOf(i2));
            default:
                return null;
        }
    }

    public static Integer mapToDrawable(int i, int i2) {
        Integer num;
        switch (i) {
            case Property.WhiteBalance /*20485*/:
                num = (Integer) nikonWhitebalanceIconsMap.get(Integer.valueOf(i2));
                return Integer.valueOf(num != null ? num.intValue() : R.drawable.whitebalance_unknown);
            case Property.ExposureMeteringMode /*20491*/:
                num = (Integer) nikonMeteringModeMap.get(Integer.valueOf(i2));
                return Integer.valueOf(num != null ? num.intValue() : R.drawable.whitebalance_unknown);
            case Property.ExposureProgramMode /*20494*/:
                num = (Integer) nikonExposureProgramMap.get(Integer.valueOf(i2));
                return Integer.valueOf(num != null ? num.intValue() : R.drawable.whitebalance_unknown);
            case Property.FocusMeteringMode /*20508*/:
                num = (Integer) nikonFocusMeteringModeIconsMap.get(Integer.valueOf(i2));
                return Integer.valueOf(num != null ? num.intValue() : R.drawable.whitebalance_unknown);
            case Property.EosShootingMode /*53509*/:
                num = (Integer) eosShootingModeIconsMap.get(Integer.valueOf(i2));
                return Integer.valueOf(num != null ? num.intValue() : R.drawable.shootingmode_unknown);
            case Property.EosDriveMode /*53510*/:
                num = (Integer) eosDriveModeIconsMap.get(Integer.valueOf(i2));
                return Integer.valueOf(num != null ? num.intValue() : R.drawable.whitebalance_unknown);
            case Property.EosMeteringMode /*53511*/:
                num = (Integer) eosMeteringModeIconsMap.get(Integer.valueOf(i2));
                return Integer.valueOf(num != null ? num.intValue() : R.drawable.whitebalance_unknown);
            case Property.EosWhitebalance /*53513*/:
                num = (Integer) eosWhitebalanceIconsMap.get(Integer.valueOf(i2));
                return Integer.valueOf(num != null ? num.intValue() : R.drawable.whitebalance_unknown);
            default:
                return null;
        }
    }

    public static String mapToString(int i, int i2, int i3) {
        int i4;
        int i5;
        int round;
        String str;
        switch (i2) {
            case Property.WhiteBalance /*20485*/:
                return (String) nikonWhitebalanceMap.get(Integer.valueOf(i3));
            case Property.FNumber /*20487*/:
                i4 = i3 / 100;
                i5 = i3 % 100;
                return i5 == 0 ? "f " + i4 : i5 % 10 == 0 ? "f " + i4 + '.' + (i5 / 10) : "f " + i4 + '.' + i5;
            case Property.FocusMode /*20490*/:
                return (String) nikonFocusModeMap.get(Integer.valueOf(i3));
            case Property.ExposureTime /*20493*/:
                if (i3 == -1) {
                    return "Bulb";
                }
                i4 = i3 / 10000;
                i5 = i3 % 10000;
                StringBuilder stringBuilder = new StringBuilder();
                if (i4 > 0) {
                    stringBuilder.append(i4).append("\"");
                }
                if (i5 > 0) {
                    stringBuilder.append("1/").append(Math.round(1.0d / (((double) i5) * 1.0E-4d)));
                }
                return stringBuilder.toString();
            case Property.ExposureIndex /*20495*/:
                return getNikonExposureIndex(i, i3);
            case Property.ExposureBiasCompensation /*20496*/:
                round = Math.round(((float) Math.abs(i3)) / 100.0f) / 10;
                char c = i3 >= 0 ? '+' : '-';
                return String.format("%c%d.%d", new Object[]{Character.valueOf(c), Integer.valueOf(round), Integer.valueOf(i5 % 10)});
            case Property.FocusMeteringMode /*20508*/:
                return (String) nikonFocusMeteringModeMap.get(Integer.valueOf(i3));
            case Property.NikonWbColorTemp /*53278*/:
                return getNikonWbColorTemp(i, i3);
            case Property.NikonShutterSpeed /*53504*/:
                i4 = (i3 >> 16) & MinElf.PN_XNUM;
                i5 = MinElf.PN_XNUM & i3;
                if (i5 == 1) {
                    return "" + i4 + "\"";
                }
                if (i4 == 1) {
                    return "1/" + i5;
                }
                if (i3 == -1) {
                    return "Bulb";
                }
                if (i3 == -2) {
                    return "Flash";
                }
                if (i4 <= i5) {
                    return "" + i4 + Operation.DIVISION + i5;
                }
                return String.format("%.1f\"", new Object[]{Double.valueOf(((double) i4) / ((double) i5))});
            case Property.EosApertureValue /*53505*/:
                Object obj = (String) eosApertureValueMap.get(Integer.valueOf(i3));
                StringBuilder append = new StringBuilder().append("f ");
                if (obj == null) {
                    obj = Character.valueOf('?');
                }
                return append.append(obj).toString();
            case Property.EosShutterSpeed /*53506*/:
                str = (String) eosShutterSpeedMap.get(Integer.valueOf(i3));
                return str == null ? Operation.EMPTY_PARAM : str;
            case Property.EosIsoSpeed /*53507*/:
                return (String) eosIsoSpeedMap.get(Integer.valueOf(i3));
            case Property.EosExposureCompensation /*53508*/:
                char c2;
                if (i3 > 128) {
                    i3 = 256 - i3;
                    c2 = '-';
                } else {
                    c2 = '+';
                }
                if (i3 == 0) {
                    return " 0";
                }
                round = i3 / 8;
                i4 = i3 % 8;
                str = i4 == 3 ? "1/3" : i4 == 4 ? "1/2" : i4 == 5 ? "2/3" : "";
                if (round > 0) {
                    return String.format("%c%d %s", new Object[]{Character.valueOf(c2), Integer.valueOf(round), str});
                }
                return String.format("%c%s", new Object[]{Character.valueOf(c2), str});
            case Property.EosShootingMode /*53509*/:
                return (String) eosShootingModeMap.get(Integer.valueOf(i3));
            case Property.EosDriveMode /*53510*/:
                return (String) eosDriveModeMap.get(Integer.valueOf(i3));
            case 53512:
                return (String) eosFocusModeMap.get(Integer.valueOf(i3));
            case Property.EosWhitebalance /*53513*/:
                return (String) eosWhitebalanceMap.get(Integer.valueOf(i3));
            case Property.EosColorTemperature /*53514*/:
                return Integer.toString(i3) + "K";
            case Property.EosPictureStyle /*53520*/:
                return (String) eosPictureStyleMap.get(Integer.valueOf(i3));
            case Property.NikonExposureIndicateStatus /*53681*/:
                return "" + (i3 / 6) + "." + (Math.abs(i3) % 6) + " EV";
            case Property.NikonActivePicCtrlItem /*53760*/:
                return (String) nikonActivePicCtrlItemMap.get(Integer.valueOf(i3));
            default:
                return Operation.EMPTY_PARAM;
        }
    }
}
