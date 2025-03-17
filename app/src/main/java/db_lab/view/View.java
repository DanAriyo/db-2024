package db_lab.view;

import db_lab.controller.Controller;
import db_lab.model.Model;

public interface View {

    public void show();

    public Controller getController();

    public Model getModel();

}
