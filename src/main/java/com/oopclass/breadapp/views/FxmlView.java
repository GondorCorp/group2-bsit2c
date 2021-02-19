    package com.oopclass.breadapp.views;

import java.util.ResourceBundle;

public enum FxmlView {

   CUSTOMER {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("customer.title");
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/Customer.fxml";
        }
    },
   EMPLOYEE {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("employee.title");
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/Employee.fxml";
        }
    }, 
   EVEENT {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("eveent.title");
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/Eveent.fxml";
        }
    };
    public abstract String getTitle();

    public abstract String getFxmlFile();

    String getStringFromResourceBundle(String key) {
        return ResourceBundle.getBundle("Bundle").getString(key);
    }

}
