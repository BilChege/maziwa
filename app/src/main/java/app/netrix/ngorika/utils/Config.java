package app.netrix.ngorika.utils;

import java.util.List;

import app.netrix.ngorika.pojo.Farmer;
import app.netrix.ngorika.pojo.InventoryItem;
import app.netrix.ngorika.pojo.MaterialRequisitionDetail;

public class Config {

    public interface ItemsListener{
        void itemSelected(InventoryItem inventoryItem);
        void itemAddedToCart(MaterialRequisitionDetail materialRequisitionDetail);
        void itemsToShow(List<InventoryItem> items);
        void checkOut();
        void cancelRequisition();
        void farmerSelected(Farmer farmer);
    }

}
