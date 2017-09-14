package com.java.fourteen.utils;

/**
 * Created by Victorywys on 2017/9/9.
 */

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.util.ResourceUtil;
import com.iflytek.cloud.util.ResourceUtil.RESOURCE_TYPE;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class SpeechUtil {
    private String TAG = "SpeechUtil";
    private Context context;

    private String app_id = "59afa1b8";

    //语音合成对象
    private SpeechSynthesizer mSpeechSynthesizer;

    // 默认云端发音人
    public static String voicerCloud="xiaoyan";
    // 默认本地发音人
    public static String voicerLocal="xiaoyan";
    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_LOCAL;

    private int speed = 50; //合成语音音速
    private int pitch = 50; //合成语音音调
    private int volume = 100; //合成语音音量,0-100
    private int stream_type = 3; //播放器的音频流类型
    private boolean isInterrupt = true; //播放合成音频打断音乐播放，默认为true
    private String audio_formal = "wav"; //音频格式，只支持wav和pcm格式

    public SpeechUtil(Context context) {
        this.context = context;
        setAppId(); //一定要在语音合成对象实例化前设置，不然会报21001错误（没有安装语音组件）
        init();
        setParams();
    }

    /**
     * 初始化语音合成
     */
    private void init() {
        mSpeechSynthesizer = SpeechSynthesizer.createSynthesizer(context, mInitListener);
    }

    /**
     * 设置语音合成的参数
     */
    private void setParams() {
        // 清空参数
        mSpeechSynthesizer.setParameter(SpeechConstant.PARAMS, null);

        //设置合成的模式
        //设置使用本地引擎
        mSpeechSynthesizer.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);

        //设置发音人资源路径
        mSpeechSynthesizer.setParameter(ResourceUtil.TTS_RES_PATH, getResourcePath());

        //设置发音人
        mSpeechSynthesizer.setParameter(SpeechConstant.VOICE_NAME, voicerLocal);

        //设置合成音速
        mSpeechSynthesizer.setParameter(SpeechConstant.SPEED, String.valueOf(speed));
        //设置合成音调
        mSpeechSynthesizer.setParameter(SpeechConstant.PITCH, String.valueOf(pitch));
        //设置合成音量
        mSpeechSynthesizer.setParameter(SpeechConstant.VOLUME, String.valueOf(volume));
        //设置播放器音频流类型
        mSpeechSynthesizer.setParameter(SpeechConstant.STREAM_TYPE, String.valueOf(stream_type));
        // 设置播放合成音频打断音乐播放，默认为true
        mSpeechSynthesizer.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, String.valueOf(isInterrupt));
        // 设置合成音频格式，只支持wav和pcm格式
        mSpeechSynthesizer.setParameter(SpeechConstant.AUDIO_FORMAT, audio_formal);

        mSpeechSynthesizer.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/xf_yuyin/tts.wav");
    }

    /**
     * 开始合成语音并播放
     * @param content 播放内容
     * @return 播放结果码
     */
    public int startSpeak(String content) {
//      setParams();
        //toastMessage("content");
        int result = mSpeechSynthesizer.startSpeaking(content, mTtsListener);
        if(result != ErrorCode.SUCCESS) {
            toastMessage("语音合成失败,错误码: " + result);
        }
        return result;
    }
    /**
     * 开始合成语音并播放
     * @param resId 播放内容的资源id
     * @return 播放结果码
     */
    public int startSpeak(int resId) {
        return startSpeak(context.getString(resId));
    }

    public void stopSpeak() {
        mSpeechSynthesizer.stopSpeaking();
    }

    /**
     * 初始化监听
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int arg0) {
            // TODO Auto-generated method stub
            Log.d(TAG, "InitListener init, code=" + arg0);
            if(arg0 != ErrorCode.SUCCESS) {
                toastMessage("初始化失败，错误码：" + arg0);
            } else {

            }
        }
    };

    /**
     * 合成回调监听。
     */
    private SynthesizerListener mTtsListener = new SynthesizerListener() {

        @Override
        public void onSpeakBegin() {
            //开始播放
        }

        @Override
        public void onSpeakPaused() {
            //暂停播放
        }

        @Override
        public void onSpeakResumed() {
            //"继续播放")
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos,
                                     String info) {
            // 合成进度

        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
            // 播放进度

        }

        @Override
        public void onCompleted(SpeechError error) {
            if (error == null) { //播放完成

            } else {
                toastMessage(error.getErrorDescription());
            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null

        }
    };

    /**
     * 获取本地assets文件夹下的发音人资源路径
     */
    private String getResourcePath() {
        StringBuffer tempBuffer = new StringBuffer();
        //合成通用资源
        tempBuffer.append(ResourceUtil.generateResourcePath(context, RESOURCE_TYPE.assets, "tts/common.jet"));
        tempBuffer.append(";");
        //发音人资源
        tempBuffer.append(ResourceUtil.generateResourcePath(context, RESOURCE_TYPE.assets, "tts/"+ voicerLocal +".jet"));
        return tempBuffer.toString();
    }

    /** 应用程序入口处调用,避免手机内存过小,杀死后台进程后通过历史intent进入Activity造成SpeechUtility对象为null
     * 注意：此接口在非主进程调用会返回null对象，如需在非主进程使用语音功能，请增加参数：SpeechConstant.FORCE_LOGIN+"=true"
     * 参数间使用“,”分隔。
     * 设置你申请的应用appid

     * 注意： appid 必须和下载的SDK保持一致，否则会出现10407错误
     *            一定要在语音合成对象实例化前设置，不然会报21001错误（没有安装语音组件）
     */
    private void setAppId() {
        StringBuffer param = new StringBuffer();
        param.append("appid="+app_id);
        param.append(",");
        // 设置使用v5+
        param.append(SpeechConstant.ENGINE_MODE+"="+SpeechConstant.MODE_MSC);
        SpeechUtility.createUtility(context, param.toString());
    }

    public void toastMessage(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
    public void toastMessage(int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
    }
}
