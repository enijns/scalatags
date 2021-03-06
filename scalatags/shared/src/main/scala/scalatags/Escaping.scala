package scalatags
import acyclic.file
/**
 * Utility methods related to validating and escaping XML; used internally but
 * potentially useful outside of Scalatags.
 */
object Escaping {

  private[this] val tagRegex = "^[a-z][\\w0-9-]*$".r

  /**
   * Uses a regex to check if something is a valid tag name.
   */
  def validTag(s: String) = tagRegex.unapplySeq(s).isDefined

  private[this] val attrNameRegex = "^[a-zA-Z_:][-a-zA-Z0-9_:.]*$".r
  /**
   * Uses a regex to check if something is a valid attribute name.
   */
  def validAttrName(s: String) = attrNameRegex.unapplySeq(s).isDefined

  /**
   * Code to escape text HTML nodes. Taken from scala.xml
   */
  def escape(text: String, s: StringBuilder) = {
    // Implemented per XML spec:
    // http://www.w3.org/International/questions/qa-controls
    // imperative code 3x-4x faster than current implementation
    // dpp (David Pollak) 2010/02/03
    val len = text.length
    var pos = 0
    var prev = 0

    @inline
    def handle(snip: String) = {
      s.append(text.substring(prev, pos))
      s.append(snip)
    }
    while (pos < len) {
      text.charAt(pos) match {
        case '<' => handle("&lt;"); prev = pos + 1
        case '>' => handle("&gt;"); prev = pos + 1
        case '&' => handle("&amp;"); prev = pos + 1
        case '"' => handle("&quot;"); prev = pos + 1
        case '\n' => handle("\n"); prev = pos + 1
        case '\r' => handle("\r"); prev = pos + 1
        case '\t' => handle("\t"); prev = pos + 1
        case c if c < ' ' => handle(""); prev = pos + 1
        case _ =>
      }
      pos += 1
    }
    handle("")
  }
}
