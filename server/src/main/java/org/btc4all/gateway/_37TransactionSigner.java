package org.btc4all.gateway;

import static com.google.common.base.Preconditions.checkArgument;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import org.spongycastle.util.encoders.Hex;

import com.google.bitcoin.core.BitcoinSerializer;
import com.google.bitcoin.core.SigningAssembly;
import com.google.bitcoin.core.Transaction;
import com.google.bitcoin.core.TransactionOutput;
import com.google.bitcoin.core.TransactionSigner;
import com.google.bitcoin.params.MainNetParams;

public class _37TransactionSigner implements TransactionSigner {

    @Override
    public void signInputs(Transaction tx, Map<TransactionOutput, SigningAssembly> signingAssemblies) {
        for (SigningAssembly constituent : signingAssemblies.values()) {
            checkArgument(constituent.getRedeemScript() != null, "37TransactionSigner needs a redeem script for multi-sig transactions");
            checkArgument(constituent.getKeys().size() > 1, "37TransactionSigner needs more than 1 key to sign a multi-sig");
        }
        BitcoinSerializer bs = new BitcoinSerializer(MainNetParams.get(), true, false);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            bs.serialize(tx, bos);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        System.out.println("tx: "+ tx);
        System.out.println(Hex.encode(bos.toByteArray()).toString());
        System.out.println("sd: "+ signingAssemblies);
    }

}
