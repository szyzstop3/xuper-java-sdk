import com.baidu.xuperunion.api.Account;
import com.baidu.xuperunion.api.Transaction;
import com.baidu.xuperunion.api.XuperClient;

import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Test {
    public static void main(String[] args) throws Exception {
      //  Create client

        XuperClient client = new XuperClient("127.0.0.1:37101");

      //  Import account keys

// Import account from local keys
        Account account = Account.create("./keys");

     //   Create contract account

// The account name is XC1111111111111111@xuper
        client.createContractAccount(account, "1111111111111111");

     //   Transfer xuper to contract account

        client.transfer(account, "XC1111111111111111@xuper", BigInteger.valueOf(1000000));

     //   Query balance of account

        BigInteger result = client.getBalance("XC1111111111111111@xuper");

     //   Deploy contract using contract account

// Using a contract account to deploy contract
        account.setContractAccount("XC1111111111111111@xuper");
        Map<String, byte[]> args1 = new HashMap<>();
        args1.put("creator", "icexin".getBytes());
        String codePath = "./counter.wasm";
        byte[] code = Files.readAllBytes(Paths.get(codePath));
// the runtime is c
        client.deployWasmContract(account, code, "counter", "c", args1);

      //  Invoke contract

        Map<String, byte[]> args2 = new HashMap<>();
        args2.put("key", "icexin".getBytes());
        Transaction tx = client.invokeContract(account, "wasm", "counter", "increase", args2);
        System.out.println("txid: " + tx.getTxid());
        System.out.println("response: " + tx.getContractResponse().getBodyStr());
        System.out.println("gas: " + tx.getGasUsed());
    }
}
