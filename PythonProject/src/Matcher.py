from abc import ABC, abstractmethod

class Matcher(ABC):

  @abstractmethod
  def match(self):
    pass