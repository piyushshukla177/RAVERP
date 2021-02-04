package com.rav.raverp.data.model.api;

import java.io.Serializable;
import java.util.ArrayList;

public class DashBoardModal implements Serializable {

    public String Response = "";
    public String Message = "";
    ArrayList<body> body;

    public String getResponse() {
        return Response;
    }

    public void setResponse(String response) {
        Response = response;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public ArrayList<DashBoardModal.body> getBody() {
        return body;
    }

    public void setBody(ArrayList<DashBoardModal.body> body) {
        this.body = body;
    }


    public class body {
        public String MyWalletAmount = "";
        public String RpWalletAmount = "";
        public String LeftBV = "";
        public String RightBV = "";
        public String TotalBV = "";
        public String book = "";
        public String hold = "";
        public String ActiveMember = "";
        public String InActiveMember = "";

        public String getMyWalletAmount() {
            return MyWalletAmount;
        }

        public void setMyWalletAmount(String myWalletAmount) {
            MyWalletAmount = myWalletAmount;
        }

        public String getRpWalletAmount() {
            return RpWalletAmount;
        }

        public void setRpWalletAmount(String rpWalletAmount) {
            RpWalletAmount = rpWalletAmount;
        }

        public String getLeftBV() {
            return LeftBV;
        }

        public void setLeftBV(String leftBV) {
            LeftBV = leftBV;
        }

        public String getRightBV() {
            return RightBV;
        }

        public void setRightBV(String rightBV) {
            RightBV = rightBV;
        }

        public String getTotalBV() {
            return TotalBV;
        }

        public void setTotalBV(String totalBV) {
            TotalBV = totalBV;
        }

        public String getBook() {
            return book;
        }

        public void setBook(String book) {
            this.book = book;
        }

        public String getHold() {
            return hold;
        }

        public void setHold(String hold) {
            this.hold = hold;
        }

        public String getActiveMember() {
            return ActiveMember;
        }

        public void setActiveMember(String activeMember) {
            ActiveMember = activeMember;
        }

        public String getInActiveMember() {
            return InActiveMember;
        }

        public void setInActiveMember(String inActiveMember) {
            InActiveMember = inActiveMember;
        }
    }
}