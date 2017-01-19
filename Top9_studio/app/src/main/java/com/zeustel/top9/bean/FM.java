package com.zeustel.top9.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 电台整体
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/8/4 15:45
 */
@Deprecated
public class FM implements Serializable {
    //节目列表
    private ArrayList<Program> programs;
    //主播
    private Compere compere;

    public Compere getCompere() {
        return compere;
    }

    public void setCompere(Compere compere) {
        this.compere = compere;
    }

    //当前播放节目id
    private Program playingProgram;

    public ArrayList<Program> getPrograms() {
        return programs;
    }

    public void setPrograms(ArrayList<Program> programs) {
        this.programs = programs;
    }

    public Program getPlayingProgram() {
        return playingProgram;
    }

    public void setPlayingProgram(Program playingProgram) {
        this.playingProgram = playingProgram;
    }
}
