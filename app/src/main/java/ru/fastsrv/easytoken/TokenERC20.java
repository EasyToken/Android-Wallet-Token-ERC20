package ru.fastsrv.easytoken;

import org.web3j.abi.EventEncoder;
import org.web3j.abi.EventValues;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

public class TokenERC20 extends Contract {
    private static final String BINARY = "6060604052341561000f57600080fd5b60405161067c38038061067c833981016040528080519190602001805182019190602001805182019190602001805160025460ff16600a0a86026003819055600160a060020a03331660009081526004602052604081209190915590925090508380516100809291602001906100b2565b5060018280516100949291602001906100b2565b506002805460ff191660ff929092169190911790555061014d915050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106100f357805160ff1916838001178555610120565b82800160010185558215610120579182015b82811115610120578251825591602001919060010190610105565b5061012c929150610130565b5090565b61014a91905b8082111561012c5760008155600101610136565b90565b6105208061015c6000396000f30060606040526004361061008d5763ffffffff7c010000000000000000000000000000000000000000000000000000000060003504166306fdde03811461009257806318160ddd1461011c57806323b872dd14610141578063313ce5671461017d57806370a08231146101a657806395d89b41146101c5578063a9059cbb146101d8578063dd62ed3e146101fc575b600080fd5b341561009d57600080fd5b6100a5610221565b60405160208082528190810183818151815260200191508051906020019080838360005b838110156100e15780820151838201526020016100c9565b50505050905090810190601f16801561010e5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b341561012757600080fd5b61012f6102bf565b60405190815260200160405180910390f35b341561014c57600080fd5b610169600160a060020a03600435811690602435166044356102c5565b604051901515815260200160405180910390f35b341561018857600080fd5b61019061033c565b60405160ff909116815260200160405180910390f35b34156101b157600080fd5b61012f600160a060020a0360043516610345565b34156101d057600080fd5b6100a5610357565b34156101e357600080fd5b6101fa600160a060020a03600435166024356103c2565b005b341561020757600080fd5b61012f600160a060020a03600435811690602435166103d1565b60008054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156102b75780601f1061028c576101008083540402835291602001916102b7565b820191906000526020600020905b81548152906001019060200180831161029a57829003601f168201915b505050505081565b60035481565b600160a060020a038084166000908152600560209081526040808320339094168352929052908120548211156102fa57600080fd5b600160a060020a03808516600090815260056020908152604080832033909416835292905220805483900390556103328484846103ee565b5060019392505050565b60025460ff1681565b60046020526000908152604090205481565b60018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156102b75780601f1061028c576101008083540402835291602001916102b7565b6103cd3383836103ee565b5050565b600560209081526000928352604080842090915290825290205481565b6000600160a060020a038316151561040557600080fd5b600160a060020a0384166000908152600460205260409020548290101561042b57600080fd5b600160a060020a0383166000908152600460205260409020548281011161045157600080fd5b50600160a060020a0380831660008181526004602052604080822080549488168084528284208054888103909155938590528154870190915591909301927fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef9085905190815260200160405180910390a3600160a060020a038084166000908152600460205260408082205492871682529020540181146104ee57fe5b505050505600a165627a7a72305820f28e3e9e2b6455a65fb06a87ec5bd61e59588cbfa6afe841f1faee8834b01ee70029";

    static TokenERC20 load(String smartcontract, Web3j web3j, PersonalUnlockAccount personalUnlockAccount, BigInteger valueOf, BigInteger valueOf0) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    protected TokenERC20(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected TokenERC20(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public List<TransferEventResponse> getTransferEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("Transfer",
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        List<EventValues> valueList = extractEventParameters(event, transactionReceipt);
        ArrayList<TransferEventResponse> responses = new ArrayList<TransferEventResponse>(valueList.size());
        for (EventValues eventValues : valueList) {
            TransferEventResponse typedResponse = new TransferEventResponse();
            typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.to = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<TransferEventResponse> transferEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("Transfer",
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, TransferEventResponse>() {
            @Override
            public TransferEventResponse call(Log log) {
                EventValues eventValues = extractEventParameters(event, log);
                TransferEventResponse typedResponse = new TransferEventResponse();
                typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.to = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public List<BurnEventResponse> getBurnEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("Burn",
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        List<EventValues> valueList = extractEventParameters(event, transactionReceipt);
        ArrayList<BurnEventResponse> responses = new ArrayList<BurnEventResponse>(valueList.size());
        for (EventValues eventValues : valueList) {
            BurnEventResponse typedResponse = new BurnEventResponse();
            typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<BurnEventResponse> burnEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("Burn",
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, BurnEventResponse>() {
            @Override
            public BurnEventResponse call(Log log) {
                EventValues eventValues = extractEventParameters(event, log);
                BurnEventResponse typedResponse = new BurnEventResponse();
                typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public RemoteCall<String> name() {
        Function function = new Function("name",
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<BigInteger> totalSupply() {
        Function function = new Function("totalSupply",
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> transferFrom(String _from, String _to, BigInteger _value) {
        Function function = new Function(
                "transferFrom",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_from),
                        new org.web3j.abi.datatypes.Address(_to),
                        new org.web3j.abi.datatypes.generated.Uint256(_value)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> decimals() {
        Function function = new Function("decimals",
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> balanceOf(String param0) {
        Function function = new Function("balanceOf",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<String> symbol() {
        Function function = new Function("symbol",
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> transfer(String _to, BigInteger _value) {
        Function function = new Function(
                "transfer",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_to),
                        new org.web3j.abi.datatypes.generated.Uint256(_value)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> allowance(String param0, String param1) {
        Function function = new Function("allowance",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(param0),
                        new org.web3j.abi.datatypes.Address(param1)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public static RemoteCall<TokenERC20> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, BigInteger initialSupply, String tokenName, String tokenSymbol, BigInteger tokendecimals) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(initialSupply),
                new org.web3j.abi.datatypes.Utf8String(tokenName),
                new org.web3j.abi.datatypes.Utf8String(tokenSymbol),
                new org.web3j.abi.datatypes.generated.Uint8(tokendecimals)));
        return deployRemoteCall(TokenERC20.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static RemoteCall<TokenERC20> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, BigInteger initialSupply, String tokenName, String tokenSymbol, BigInteger tokendecimals) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(initialSupply),
                new org.web3j.abi.datatypes.Utf8String(tokenName),
                new org.web3j.abi.datatypes.Utf8String(tokenSymbol),
                new org.web3j.abi.datatypes.generated.Uint8(tokendecimals)));
        return deployRemoteCall(TokenERC20.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static TokenERC20 load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new TokenERC20(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static TokenERC20 load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new TokenERC20(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static class TransferEventResponse {
        public String from;

        public String to;

        public BigInteger value;
    }

    public static class BurnEventResponse {
        public String from;

        public BigInteger value;
    }
}
