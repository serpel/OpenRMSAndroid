package com.intellisysla.rmsscanner.BLL;

import com.intellisysla.rmsscanner.DAL.Department;
import com.intellisysla.rmsscanner.DAL.Item;

import java.util.ArrayList;

/**
 * Created by alienware on 12/27/2016.
 */

public interface ActivityCommunicator{
    void passDataToActivity(Item item);
    void Clear();
}