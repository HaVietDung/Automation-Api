package utils;

import com.jcraft.jsch.*;
import config.ConfigResource;
import config.PriceConfig;
import constants.CommonConstant;
import constants.SessionVariable;
import net.thucydides.core.annotations.Steps;
import org.junit.Assert;
import smartosc.lgeobs.screenplay.actions.ActionCommon;
import smartosc.lgeobs.screenplay.tasks.Start;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class CronJobHelper extends ActionCommon {
    static int timeout = 0;

    public static void disConnect() {
        DatabaseConnection.getInstance().closeConnection();
    }

    public static void run(String command) throws JSchException, IOException {
        System.out.println("Command: " + command);
        ChannelExec channel = (ChannelExec) DatabaseConnection.getInstance().getSession().openChannel("exec");
        channel.setInputStream(null);
        (channel).setErrStream(System.err);
        (channel).setCommand(command);
        channel.connect();

        InputStream in = channel.getInputStream();
        byte[] tmp = new byte[1024];
        int timeout = 60; // timeout 60s

        while (true) {
            while (in.available() > 0) {
                int i = in.read(tmp, 0, 1024);
                if (i < 0) break;
                System.out.print(new String(tmp, 0, i));
            }
            if (channel.isClosed()) {
                System.out.println("exit-status: " + channel.getExitStatus());
                break;
            }
            pause(1000, "Wait " + command);
            timeout--;
            if (timeout == 0 && command.contains("queue:consumers:start")) {
                System.out.println("Timeout run: " + command);
                break;
            }
        }
    }

    public static void run(String user, String command) throws JSchException, IOException {
        System.out.println("Command: " + command);
        ChannelExec channel = (ChannelExec) DatabaseConnection.getInstance().getSession(user).openChannel("exec");
        channel.setInputStream(null);
        (channel).setErrStream(System.err);
        (channel).setCommand(command);
        channel.connect();

        InputStream in = channel.getInputStream();
        byte[] tmp = new byte[1024];
        int timeout = 60; // timeout 60s

        while (true) {
            while (in.available() > 0) {
                int i = in.read(tmp, 0, 1024);
                if (i < 0) break;
                System.out.print(new String(tmp, 0, i));
            }
            if (channel.isClosed()) {
                System.out.println("exit-status: " + channel.getExitStatus());
                break;
            }
            pause(1000, "Wait " + command);
            timeout--;
            if (timeout == 0 && command.contains("queue:consumers:start")) {
                System.out.println("Timeout run: " + command);
                break;
            }
        }
    }

    public static void runCron(String command) {
        try {
            run(command);
        } catch (JSchException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void runCron(String user, String command) {
        try {
            run(user, command);
        } catch (JSchException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void getExportOrderFile(String user, String remoteFile, String localFile, String fileType) {
        try {
            Channel sftp = DatabaseConnection.getInstance().getSession(user).openChannel("sftp");
            // 5 seconds timeout
            sftp.connect(1000);

            ChannelSftp channelSftp = (ChannelSftp) sftp;
            if (("ORDER,ORDER_CANCEL").contains(fileType)) {
                channelSftp.cd(Start.getPathOrderExport());
            } else if (fileType.equals("RMA") || fileType.equals("DR") || fileType.equals("AuPaypal")) {
                channelSftp.cd("var/log/");
            } else if (fileType.equals("PANTHER")) {
                channelSftp.cd("var/log/");
            }

            // download file from a remote server to local
            channelSftp.get(remoteFile, localFile);
            channelSftp.exit();

            System.out.println("Get " + remoteFile + " - " + user + " done!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getLogFile(String fileType) throws IOException, JSchException, SftpException {
        ActionCommon actionCommon = new ActionCommon();
        timeout++;
        String timeFile = actionCommon.convertTimestampToDate(System.currentTimeMillis(), "yyyyMMdd");
        String orderNumber = getSessionVariable(SessionVariable.Order.ORDER_NUMBER);
        new SQLHelper().getOrderIdByOrderNumber(orderNumber);
        String orderId = getSessionVariable(SessionVariable.Order.ORDER_ID);
        String remoteFile = "order-export.log";
        String jsonOrderExportFile = "src/test/resources/data/json/order_creation_" + orderNumber + ".json";

        switch (fileType) {
            case "RMA":
                orderNumber = getSessionVariable(SessionVariable.Return.RETURN_NUMBER);
                remoteFile = "return_management.log";
                jsonOrderExportFile = "src/test/resources/data/json/rma_export_" + orderNumber + ".json";
                break;
            case "PANTHER":
                remoteFile = "panther.log";
                jsonOrderExportFile = "src/test/resources/data/json/panther_export_" + orderNumber + ".json";
                break;
            case "DR":
                remoteFile = "drlog.log";
                jsonOrderExportFile = "src/test/resources/data/json/drlog_" + orderId + ".json";
                break;
            case "AuPaypal":
                remoteFile = "aupaypal.log";
                jsonOrderExportFile = "src/test/resources/data/json/aupaypal_log_" + orderNumber + ".json";
                break;
        }
//        else if (fileType.equals("ORDER_CANCEL")){
//            jsonOrderExportFile = "src/test/resources/data/json/cancel_export_" + orderNumber + ".json";
//        }

        String localFile = "src/test/resources/data/log/" + remoteFile;
        System.out.println(localFile);
        File logFile = new File(localFile);
        if (logFile.createNewFile()) {
            System.out.println("File created");
        } else {
            System.out.println("File already exists");
        }
        String userTemp = Start.getUserMagentoCloud();
        String user = userTemp;
        boolean isChildUser = false;
        System.out.println("User magento cloud: " + user);
        boolean hasLogFile = false;
        int indexUser = 0;
        while (indexUser < 6) {
            if (userTemp.contains(".")) {
                user = userTemp.substring(1);
                isChildUser = true;
            }
            indexUser++;
            System.out.println("index user: " + indexUser);
            if (isChildUser) {
                user = indexUser + user;
            }
            getExportOrderFile(user, remoteFile, localFile, fileType);
            String contentExportOrder = new String(Files.readAllBytes(Paths.get(localFile)), StandardCharsets.UTF_8);
            if ((("ORDER,RMA,PANTHER").contains(fileType) && contentExportOrder.contains(orderNumber))
                    || (fileType.equals("ORDER_CANCEL") && (contentExportOrder.contains(orderNumber)) && contentExportOrder.contains("\"CANCEL\"")
                    || (fileType.equals("DR")) && contentExportOrder.contains(orderId) && contentExportOrder.contains("refundedAmount"))
                    || (fileType.equals("AuPaypal")) && contentExportOrder.contains(orderNumber) && contentExportOrder.contains("total_refunded_amount")
            ) {
                System.out.println(orderNumber + " - in user: " + user);
                //new input
                try (BufferedReader br = new BufferedReader(new FileReader(localFile))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        if (
                                (fileType.equals("ORDER") && line.contains("\"ORDER_" + orderNumber)) ||
                                        (fileType.equals("RMA") && line.contains("\"RETURN_" + orderNumber))
                                        || (fileType.equals("ORDER_CANCEL") && line.contains(orderNumber) && line.contains("\"CANCEL\"")
                                        || (fileType.equals("DR")) && line.contains(orderId) && line.contains("refundedAmount"))
                                        || (fileType.equals("AuPaypal")) && line.contains(orderNumber) && line.contains("total_refunded_amount")
                        ) {
                            System.out.println("Index of line = " + line.indexOf("{"));
                            String jsonLine = line.substring(line.indexOf("{")).replaceAll("\\\\", "");
                            PrintWriter file = new PrintWriter(jsonOrderExportFile, "UTF-8");
                            PrintWriter fileJp = new PrintWriter(jsonOrderExportFile);
                            if (jsonLine.contains("[] []"))
                                jsonLine = jsonLine.replaceAll(" \\[] \\[]", "");
                            if (fileType.equals("AuPaypal"))
                                jsonLine = jsonLine.replaceAll("\\[]", "");
                            String country = ActionCommon.getSessionVariable(SessionVariable.Common.COUNTRY);
                            if (country.equals("jp")) {
                                fileJp.println(jsonLine);
                                fileJp.close();
                            } else {
                                file.println(jsonLine);
                                file.close();
                            }

                            System.out.println(jsonLine);
                            hasLogFile = true;
                            break;
                        } else if (fileType.equals("PANTHER") && line.contains(orderNumber)) {
                            System.out.println("Index of line = " + line.indexOf("{"));
                            String jsonLine = line.substring(line.indexOf("{"));
                            PrintWriter file = new PrintWriter(jsonOrderExportFile, "UTF-8");
                            file.println(jsonLine);
                            file.close();
                            System.out.println(jsonLine);
                            hasLogFile = true;
                            break;
                        }
                    }
                } // end new input
                if (hasLogFile)
                    break;
            }
        }

        if (!hasLogFile) {
            if (timeout < 10) { // 30s
                System.out.println("Wait get order log file = " + timeout);
                getLogFile(fileType);
            }
        }
        if (logFile.delete()) {
            System.out.println("Log file deleted successfully");
        } else {
            System.out.println("Failed to delete log file");
        }
    }

    public static void getStockMiddlewareLogFile(String fileType) throws IOException, JSchException, SftpException {
        ActionCommon actionCommon = new ActionCommon();
        timeout++;
        String timeFile = actionCommon.convertTimestampToDate(System.currentTimeMillis(), "yyyy-MM-dd");
        String stock = getSessionVariable(SessionVariable.Stock.QTY) + ".0000";
        String stockStatus = "";
        if (!stock.equals("0.0000")) {
            stockStatus = "IN_STOCK";
        } else {
            stockStatus = "OUT_OF_STOCK";
        }
        System.out.println("Stock: " + stock);
        System.out.println("Stock status: " + stockStatus);
        String country = getSessionVariable(SessionVariable.Common.COUNTRY);
        String modelCode = getSessionVariable(CommonConstant.SessionVariable.MODEL_CODE).replaceAll("\\.", "_");
        String sku = getSessionVariable(SessionVariable.Product.SKU);
        System.out.println("SKU: " + sku);
        String remoteFile = "request.log";
        String jsonExportFile = "src/test/resources/data/json/middleware_export_" + modelCode + ".json";

        String localFile = "src/test/resources/data/log/" + remoteFile;
        System.out.println(localFile);
        File logFile = new File(localFile);
        if (logFile.createNewFile()) {
            System.out.println("File created");
        } else {
            System.out.println("File already exists");
        }
        String userTemp = Start.getUserMagentoCloud();
        String user = userTemp;
        boolean isChildUser = false;
        System.out.println("User magento cloud: " + user);
        boolean hasLogFile = false;
        int indexUser = 0;
        while (indexUser < 3) {
            if (userTemp.contains(".")) {
                user = userTemp.substring(1);
                isChildUser = true;
            }
            indexUser++;
            System.out.println("index user: " + indexUser);
            if (isChildUser) {
                user = indexUser + user;
            }
            getExportMiddlewareFile(user, remoteFile, localFile, fileType);
            String contentExportFile = new String(Files.readAllBytes(Paths.get(localFile)), StandardCharsets.UTF_8);
            if (("MIDDLEWARE").contains(fileType)
                    && contentExportFile.contains("OMS_STOCK")
                    && contentExportFile.contains(country.toUpperCase())
                    && contentExportFile.contains(sku)
                    && contentExportFile.contains(stockStatus)
                    && contentExportFile.contains(stock)) {
                System.out.println(modelCode + " - in user: " + user);
                //new input
                try (BufferedReader br = new BufferedReader(new FileReader(localFile))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        if (fileType.equals("MIDDLEWARE")
                                && line.contains(sku)
                                && line.contains(stockStatus)
                                && line.contains(stock)) {
                            System.out.println("Index of line = " + line.indexOf("{"));
                            String jsonLine = line.substring(line.indexOf("{")).replaceAll("\\\\", "");
                            PrintWriter file = new PrintWriter(jsonExportFile, "UTF-8");
                            PrintWriter fileJp = new PrintWriter(jsonExportFile);
                            if (jsonLine.contains("[] []"))
                                jsonLine = jsonLine.replaceAll(" \\[] \\[]", "");
                            if (country.equals("jp")) {
                                fileJp.println(jsonLine);
                                fileJp.close();
                            } else {
                                file.println(jsonLine);
                                file.close();
                            }

                            System.out.println(jsonLine);
                            hasLogFile = true;
                            break;
                        }
                    }
                } // end new input
                if (hasLogFile)
                    break;
            }
        }

        if (!hasLogFile) {
            if (timeout < 5) { // 30s
                System.out.println("Wait get order log file = " + timeout);
                getStockMiddlewareLogFile(fileType);
            }
        }
        if (logFile.delete()) {
            System.out.println("Log file deleted successfully");
        } else {
            System.out.println("Failed to delete log file");
        }
        Assert.assertTrue(hasLogFile);
    }

    public static void getExportMiddlewareFile(String user, String remoteFile, String localFile, String fileType) {
        try {
            Channel sftp = DatabaseConnection.getInstance().getSession(user).openChannel("sftp");
            // 5 seconds timeout
            sftp.connect(1000);

            ChannelSftp channelSftp = (ChannelSftp) sftp;
            if (("MIDDLEWARE").contains(fileType)) {
                channelSftp.cd("var/log/middleware");
            }

            // download file from remote server to local
            channelSftp.get(remoteFile, localFile);
            channelSftp.exit();

            System.out.println("Get " + remoteFile + " - " + user + " done!");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            disConnect();
        }
    }


    public static void getPriceMiddlewareLogFile(String fileType) throws IOException, JSchException, SftpException {
        ActionCommon actionCommon = new ActionCommon();
        timeout++;
        String timeFile = actionCommon.convertTimestampToDate(System.currentTimeMillis(), "yyyy-MM-dd");
        String price = "";
        String customerType = getSessionVariable(SessionVariable.Customer.TYPE_ACCOUNT);
        if (customerType.equals("B2C") || customerType.equals("guest")) {
//            price = getSessionVariable(SessionVariable.CustomerGroupPrice.B2C_PRICE);
            price = getSessionVariable(SessionVariable.CustomerGroupPrice.GUEST_PRICE);
        } else if (customerType.equals("B2B2C")) {
            price = getSessionVariable(SessionVariable.CustomerGroupPrice.B2B2C_PRICE);
        } else if (customerType.equals("B2E")) {
            price = getSessionVariable(SessionVariable.CustomerGroupPrice.B2E_PRICE);
        } else if (customerType.equals("B2P")) {
            price = getSessionVariable(SessionVariable.CustomerGroupPrice.B2P_PRICE);
        }
        actionCommon.printAndReportData(customerType + " price in Log: ", price);

        String country = getSessionVariable(SessionVariable.Common.COUNTRY);
        String modelCode = getSessionVariable(CommonConstant.SessionVariable.MODEL_CODE).replaceAll("\\.", "_");
        String sku = getSessionVariable(SessionVariable.Product.SKU);
        System.out.println("SKU: " + sku);
        String remoteFile = "request.log";
        String jsonExportFile = "src/test/resources/data/json/middleware_export_" + modelCode + ".json";

        String localFile = "src/test/resources/data/log/" + remoteFile;
        System.out.println(localFile);
        File logFile = new File(localFile);
        if (logFile.createNewFile()) {
            System.out.println("File created");
        } else {
            System.out.println("File already exists");
        }
        String userTemp = Start.getUserMagentoCloud();
        String user = userTemp;
        boolean isChildUser = false;
        System.out.println("User magento cloud: " + user);
        boolean hasLogFile = false;
        int indexUser = 0;
        while (indexUser < 3) {
            if (userTemp.contains(".")) {
                user = userTemp.substring(1);
                isChildUser = true;
            }
            indexUser++;
            System.out.println("index user: " + indexUser);
            if (isChildUser) {
                user = indexUser + user;
            }
            getExportMiddlewareFile(user, remoteFile, localFile, fileType);
            String contentExportFile = new String(Files.readAllBytes(Paths.get(localFile)), StandardCharsets.UTF_8);
            if (("MIDDLEWARE").contains(fileType)
                    && contentExportFile.contains("OMS_PRICE")
                    && contentExportFile.contains(country.toUpperCase())
                    && contentExportFile.contains(sku)
                    && contentExportFile.contains(price)) {
                System.out.println(modelCode + " - in user: " + user);
                //new input
                try (BufferedReader br = new BufferedReader(new FileReader(localFile))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        if (fileType.equals("MIDDLEWARE")
                                && line.contains(sku)
                                && line.contains(price)) {
                            System.out.println("Index of line = " + line.indexOf("{"));
                            String jsonLine = line.substring(line.indexOf("{")).replaceAll("\\\\", "");
                            PrintWriter file = new PrintWriter(jsonExportFile, "UTF-8");
                            PrintWriter fileJp = new PrintWriter(jsonExportFile);
                            if (jsonLine.contains("[] []"))
                                jsonLine = jsonLine.replaceAll(" \\[] \\[]", "");
                            if (country.equals("jp")) {
                                fileJp.println(jsonLine);
                                fileJp.close();
                            } else {
                                file.println(jsonLine);
                                file.close();
                            }

                            System.out.println(jsonLine);
                            hasLogFile = true;
                            break;
                        }
                    }
                } // end new input
                if (hasLogFile)
                    break;
            }
        }

        if (!hasLogFile) {
            if (timeout < 5) { // 30s
                System.out.println("Wait get order log file = " + timeout);
                getPriceMiddlewareLogFile(fileType);
            }
        }
        if (logFile.delete()) {
            System.out.println("Log file deleted successfully");
        } else {
            System.out.println("Failed to delete log file");
        }
        Assert.assertTrue(hasLogFile);
    }

    public static void getLogFileWithoutSaveFile(String fileType) throws IOException, JSchException, SftpException {
        timeout++;
        String orderNumber = ActionCommon.getSessionVariable(SessionVariable.Order.ORDER_NUMBER);
        String fileNameSever = "order-export.log";
        String remoteFile = Start.getPathOrderExport() + fileNameSever;
        String jsonOrderExportFile = "src/test/resources/data/json/order_creation_" + orderNumber + ".json";

        String userTemp = Start.getUserMagentoCloud();
        String user = userTemp;
        boolean isChildUser = false;
        Boolean getFileSuccess = false;

        for (int numberUser = 1; numberUser <= 3; numberUser++) {
            if (userTemp.contains(".")) {
                user = userTemp.substring(1);
                isChildUser = true;
            }
            if (isChildUser) {
                user = numberUser + user;
            }

            String order = getExportOrderLogFileWithGrep( user, remoteFile);
            if (order.contains(orderNumber)) {
                BufferedReader br = new BufferedReader(new StringReader(order));
                String line;
                while ((line = br.readLine()) != null) {
                    String jsonLine = line.substring(line.indexOf("{"));
                    if (fileType.equals("ORDER")
                            || (fileType.equals("ORDER_CANCEL") && jsonLine.contains("\"CANCEL\""))) {
                        if (jsonLine.contains("[] []"))
                            jsonLine = jsonLine.replaceAll(" \\[] \\[]", "");
                        PrintWriter file = new PrintWriter(jsonOrderExportFile, "UTF-8");
                        file.println(jsonLine);
                        file.close();
                        getFileSuccess = true;
                        System.out.println(jsonLine);
                        break;
                    }
                }
            }
        }
        if (!getFileSuccess) {
            if (timeout < 30) { // 5p
                pause(800);
                System.out.println("Wait get order log file = " + timeout);
                getLogFileWithoutSaveFile(fileType);
            }
        }
    }

    public static String getExportOrderLogFileWithGrep(String user, String fileName) {
        String output = "";
        String command = "cat " + fileName + " | grep  \"" + ActionCommon.getSessionVariable(SessionVariable.Order.ORDER_NUMBER) + "\"";
        try {
            output = output + ExecuteCommandWithResponse(command, user);
        } catch (JSchException | IOException e) {
            e.printStackTrace();
        }
        return output;
    }

    public static String ExecuteCommandWithResponse(String command, String user) throws JSchException, IOException {
        String output = "";
        try {
            ChannelExec channel = (ChannelExec) DatabaseConnection.getInstance().getSession(user).openChannel("exec");
            InputStream in = channel.getInputStream();
            InputStream err = channel.getExtInputStream();
            System.out.println(command);
            channel.setCommand(command);
            channel.setInputStream(null);
            channel.setErrStream(System.err);
            channel.connect();

            ByteArrayOutputStream outputBuffer = new ByteArrayOutputStream();
            ByteArrayOutputStream errorBuffer = new ByteArrayOutputStream();
            byte[] tmp = new byte[1024];
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0) break;
                    outputBuffer.write(tmp, 0, i);
                }
                while (err.available() > 0) {
                    int i = err.read(tmp, 0, 1024);
                    if (i < 0) break;
                    errorBuffer.write(tmp, 0, i);
                }
                if (channel.isClosed()) {
                    if ((in.available() > 0) || (err.available() > 0)) continue;
                    System.out.println("exit-status: " + channel.getExitStatus());
                    break;
                }
            }
            output = outputBuffer.toString("UTF-8");
            channel.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }
}
