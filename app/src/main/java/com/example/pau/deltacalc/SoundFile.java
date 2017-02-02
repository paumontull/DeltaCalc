package com.example.pau.deltacalc;

import java.io.File;

/**
 * Created by Pau Montull i Jové on 1/2/17.
 */

public class SoundFile {

    private File file;

    private int color = R.color.colorAccent;

    public SoundFile(File file){
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public String getName(){
        return file.getName().substring(0, file.getName().lastIndexOf("."));
    }

    public void setFile(File file) {
        this.file = file;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
