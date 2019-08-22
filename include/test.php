
<!DOCTYPE html>
<html>


<body>
<?php

define( 'ABSPATH', dirname( __FILE__ ) . '/' );
function wp_guess_url() {
	$abspath_fix         = str_replace( '\\', '/', ABSPATH );
	$script_filename_dir = dirname( $_SERVER['SCRIPT_FILENAME'] );

	// The request is for the admin
	if ( strpos( $_SERVER['REQUEST_URI'], 'wp-admin' ) !== false || strpos( $_SERVER['REQUEST_URI'], 'wp-login.php' ) !== false ) {
		$path = preg_replace( '#/(wp-admin/.*|wp-login.php)#i', '', $_SERVER['REQUEST_URI'] );

		// The request is for a file in ABSPATH
	} elseif ( $script_filename_dir . '/' == $abspath_fix ) {
		// Strip off any file/query params in the path
		$path = preg_replace( '#/[^/]*$#i', '', $_SERVER['PHP_SELF'] );

	} else {
		if ( false !== strpos( $_SERVER['SCRIPT_FILENAME'], $abspath_fix ) ) {
			// Request is hitting a file inside ABSPATH
			$directory = str_replace( ABSPATH, '', $script_filename_dir );
			// Strip off the sub directory, and any file/query params
			$path = preg_replace( '#/' . preg_quote( $directory, '#' ) . '/[^/]*$#i', '', $_SERVER['REQUEST_URI'] );
		} elseif ( false !== strpos( $abspath_fix, $script_filename_dir ) ) {
			// Request is hitting a file above ABSPATH
			$subdirectory = substr( $abspath_fix, strpos( $abspath_fix, $script_filename_dir ) + strlen( $script_filename_dir ) );
			// Strip off any file/query params from the path, appending the sub directory to the installation
			$path = preg_replace( '#/[^/]*$#i', '', $_SERVER['REQUEST_URI'] ) . $subdirectory;
		} else {
			$path = $_SERVER['REQUEST_URI'];
		}
	}

	$schema = 'http://'; // set_url_scheme() is not defined yet
	$url    = $schema . $_SERVER['HTTP_HOST'] . $path;

	return rtrim( $url, '/' );
}

/*echo "HEY: ";
echo wp_guess_url();

foreach($_SERVER as $key => $value) {
	echo "<br/>" . $key . " == " . $value;
	echo "<br/><br/>";
}*/

//phpinfo();

//mysqli_real_connect( $this->dbh, $host, $this->dbuser, $this->dbpassword, null, $port, $socket, $client_flags );
var_dump($_COOKIE);

?>
<form action="/_internal/test.php" method="post">
  First name:<br>
  <input type="text" name="firstname" value="Mickey"><br>
  Last name:<br>
  <input type="text" name="lastname" value="Mouse"><br><br>
  <input type="submit" value="Submit">
</form> 
</body>


</html>