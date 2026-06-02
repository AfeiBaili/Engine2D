package cn.afeibaili.gl.logger


/**
 * # 日志工厂
 *
 *@author AfeiBaili
 *@version 2026/6/2 21:32
 */

interface LoggerFactory {
    companion object {
        fun create(name: String): Logger = LoggerImpl(name)
    }
}