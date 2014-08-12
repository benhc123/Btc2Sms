package org.btc4all.gateway;

import static com.google.bitcoin.core.Coin.CENT;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HttpContext;
import org.btc4all.gateway.pojo.WalletRequest;
import org.btc4all.gateway.resources.WalletResource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.bitcoin.core.AbstractWalletEventListener;
import com.google.bitcoin.core.Address;
import com.google.bitcoin.core.AddressFormatException;
import com.google.bitcoin.core.BlockChain;
import com.google.bitcoin.core.Coin;
import com.google.bitcoin.core.InsufficientMoneyException;
import com.google.bitcoin.core.NetworkParameters;
import com.google.bitcoin.core.PeerGroup;
import com.google.bitcoin.core.Sha256Hash;
import com.google.bitcoin.core.Transaction;
import com.google.bitcoin.core.Wallet;
import com.google.bitcoin.core.Wallet.SendRequest;
import com.google.bitcoin.crypto.DeterministicKey;
import com.google.bitcoin.crypto.MnemonicCode;
import com.google.bitcoin.params.TestNet3Params;
import com.google.bitcoin.signers.TestP2SHTransactionSigner;
import com.google.bitcoin.store.BlockStore;
import com.google.bitcoin.store.BlockStoreException;
import com.google.bitcoin.store.MemoryBlockStore;
import com.google.bitcoin.wallet.DeterministicKeyChain;
import com.google.bitcoin.wallet.DeterministicSeed;
import com.google.bitcoin.wallet.KeyChain.KeyPurpose;
import com.google.bitcoin.wallet.KeyChainGroup;
import com.google.common.collect.ImmutableList;

public class WalletClient {
    private HttpClient httpClient;
    private String url;
    
    
    public static boolean isSucceed(HttpResponse response) {
        return response.getStatusLine().getStatusCode() >= 200 && response.getStatusLine().getStatusCode() < 300;
    }
    
    public static HttpClientBuilder getClientBuilder() {
        return HttpClientBuilder.create()
                .setRedirectStrategy(new DefaultRedirectStrategy() {
                    @Override
                    public boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context) throws ProtocolException {
                        return response.getStatusLine().getStatusCode() == 308 || super.isRedirected(request, response, context);
                    }
                });
    }
    
    public WalletClient(String url) {
        httpClient = getClientBuilder().build();
        this.url = url;
    }
    
    public WalletRequest get(String account) throws IOException{
        HttpGet req = new HttpGet(url+"/account/"+account);
        return getPayload(req, WalletRequest.class);
    }
    
    public WalletRequest create(String xpub) throws IOException{
        HttpPost req = new HttpPost(url);
        String reqValue = null;
        try {
            reqValue = new ObjectMapper().writeValueAsString(new WalletRequest().setXpub(xpub));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new IOException(e);
        }
        StringEntity entity = new StringEntity(reqValue, "UTF-8");
        entity.setContentType("application/json");
        req.setEntity(entity);
        return getPayload(req, WalletRequest.class);
    }
    
    protected <K> K getPayload(HttpRequestBase request, Class<K> entityClass) throws IOException {
        HttpResponse response;
        try {
            response = httpClient.execute(request);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        if (isSucceed(response) && request.getMethod()!="DELETE"){
            return parsePayload(response, entityClass);
        }else if (isSucceed(response)){
            return null;
        }else{
            throw new IOException(response.getStatusLine().getReasonPhrase());
        }
    }
    
    protected <K> K parsePayload(HttpResponse response, Class<K> entityClass) throws IOException {
        return new ObjectMapper().readValue(response.getEntity().getContent(), entityClass);
    }

}
