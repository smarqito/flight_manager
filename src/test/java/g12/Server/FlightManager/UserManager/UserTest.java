package g12.Server.FlightManager.UserManager;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import org.junit.Test;

public class UserTest {
    @Test
    public void testIsPassValid() {

        Algorithm alg = Algorithm.HMAC256("secret");
        String token = JWT.create().withClaim("User", "marcoSousa").withClaim("isAdmin", true).sign(alg);
        System.out.println(token.length());
        token = JWT.create().withClaim("User", "marcoSousaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa").sign(alg);
        System.out.println(token.length());
        //
        JWTVerifier verifier = JWT.require(alg)
                .build();
        DecodedJWT decoded = verifier.verify(token);
        System.out.println(decoded.toString());
        Claim c = decoded.getClaim("User");
        String username =  c.asString();
        System.out.println(username);
    }
}
