package data



sealed class Tool
class TPen(val pen: data.Pen) : Tool()
class Eraser : Tool()
class Selector : Tool()
