package expenses.cli;

import org.apache.sshd.common.keyprovider.KeyPairProvider;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.session.ServerSession;
import org.apache.sshd.server.shell.ProcessShellFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Esesejcz {

    private final static Logger log = LoggerFactory.getLogger(Esesejcz.class);

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        try (var sshd = SshServer.setUpDefaultServer();
             var in = new BufferedReader(new InputStreamReader(System.in))) {

            var pair = KeyPairGenerator.getInstance("RSA").generateKeyPair();
            log.info("public key fingerprint: {}", fingerprint((RSAPublicKey) pair.getPublic()));
            sshd.setKeyPairProvider(KeyPairProvider.wrap(pair));
            sshd.setPort(1022);
            sshd.setPasswordAuthenticator(Esesejcz::authenticate);
            sshd.setShellFactory(new ProcessShellFactory("/bin/zsh -i -l", "/bin/zsh", "-i", "-l"));
            sshd.start();
            var s = in.readLine();
            while (s != null && !"exit".equalsIgnoreCase(s)) {
                s = in.readLine();
            }
        }
    }

    /**
     * <a href="https://stackoverflow.com/a/51061419">How to Calculate Fingerprint From SSH RSA Public Key in Java?</a>
     * @param pub
     * @return
     */
    private static String fingerprint(RSAPublicKey pub) {
        var n = pub.getModulus().toByteArray();
        var e = pub.getPublicExponent().toByteArray();
        var t = "ssh-rsa".getBytes(UTF_8);
        var os = new ByteArrayOutputStream();
        var dos = new DataOutputStream(os);
        try {
            dos.writeInt(t.length);
            dos.write(t);
            dos.writeInt(e.length);
            dos.write(e);
            dos.writeInt(n.length);
            dos.write(n);
            return Base64.getMimeEncoder().encodeToString(MessageDigest.getInstance("SHA-256").digest(os.toByteArray()));
        } catch (IOException | NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static boolean authenticate(String u, String p, ServerSession s) {
        log.info("authenticating: {}", u);
        return true;
    }
}
