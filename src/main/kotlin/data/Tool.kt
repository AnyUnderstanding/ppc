package data



sealed class Tool
class TPen(val pen: Pen) : Tool()
class Eraser : Tool()
class Selector : Tool()
