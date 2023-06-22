import java.util.Iterator;
import java.util.NoSuchElementException;
public class MovieDB {
	MyLinkedList<MyLinkedList<MovieDBItem>> db;
    public MovieDB() {
		db=new MyLinkedList<MyLinkedList<MovieDBItem>>();
    }

    public void insert(MovieDBItem item) {
		Node<MyLinkedList<MovieDBItem>> tempListNode=db.head;
		while(tempListNode.getNext()!=null) {
			if(tempListNode.getNext().getItem().head.getNext().getItem().getGenre().compareTo(item.getGenre())==0){
				Node<MovieDBItem> tempItemNode=tempListNode.getNext().getItem().head;
				while(tempItemNode.getNext()!=null){
					if(tempItemNode.getNext().getItem().getTitle().compareTo(item.getTitle())==0)
						return;
					else if(tempItemNode.getNext().getItem().getTitle().compareTo(item.getTitle())>0) {
						MovieDBItem newData = new MovieDBItem(item.getGenre(), item.getTitle());
						tempItemNode.insertNext(newData);
						return;
					}
					tempItemNode=tempItemNode.getNext();
				}
				MovieDBItem newData = new MovieDBItem(item.getGenre(), item.getTitle());
				tempItemNode.insertNext(newData);
				return;

			}
			else if(tempListNode.getNext().getItem().head.getNext().getItem().getGenre().compareTo(item.getGenre())>0)
			{
				MyLinkedList newList=new MyLinkedList<MovieDBItem>();
				MovieDBItem newData=new MovieDBItem(item.getGenre(), item.getTitle());
				newList.head.insertNext(newData);
				tempListNode.insertNext(newList);
				return;
			}
			tempListNode=tempListNode.getNext();
		}
		MyLinkedList newList=new MyLinkedList<MovieDBItem>();
		MovieDBItem newData=new MovieDBItem(item.getGenre(), item.getTitle());
		newList.head.insertNext(newData);
		tempListNode.insertNext(newList);
    }

    public void delete(MovieDBItem item) {
		Node<MyLinkedList<MovieDBItem>> tempListNode=db.head;

		while(tempListNode.getNext()!=null) {
			tempListNode=tempListNode.getNext();
			if(tempListNode.getItem().head.getNext().getItem().getGenre().compareTo(item.getGenre())==0){
				Node<MovieDBItem> tempItemNode=tempListNode.getItem().head;
				while(tempItemNode.getNext()!=null){
					if(tempItemNode.getNext().getItem().getTitle().compareTo(item.getTitle())==0) {
						tempItemNode.removeNext();
						break;
					}
					tempItemNode=tempItemNode.getNext();
				}
			}
		}
		tempListNode=db.head;
		while(tempListNode.getNext()!=null) {
			if(tempListNode.getNext().getItem().head.getNext()==null){
				tempListNode.removeNext();
				break;
			}
			tempListNode=tempListNode.getNext();
		};
    }

    public MyLinkedList<MovieDBItem> search(String term) {
		MyLinkedList<MovieDBItem> results = new MyLinkedList<MovieDBItem>();
		Node<MyLinkedList<MovieDBItem>> tempListNode=db.head;
		while(tempListNode.getNext()!=null) {
			tempListNode=tempListNode.getNext();
			Node<MovieDBItem> tempItemNode=tempListNode.getItem().head;
			while(tempItemNode.getNext()!=null){
				tempItemNode=tempItemNode.getNext();
				if(tempItemNode.getItem().getTitle().contains(term)) {
					MovieDBItem item=new MovieDBItem(tempItemNode.getItem().getGenre(), tempItemNode.getItem().getTitle());
					results.add(item);
				}
			}
		}
        return results;
    }

    public MyLinkedList<MovieDBItem> items() {
		MyLinkedList<MovieDBItem> results = new MyLinkedList<MovieDBItem>();
		Node<MyLinkedList<MovieDBItem>> tempListNode=db.head;
		while(tempListNode.getNext()!=null) {
			tempListNode=tempListNode.getNext();
			Node<MovieDBItem> tempItemNode=tempListNode.getItem().head;
			while(tempItemNode.getNext()!=null){
				tempItemNode=tempItemNode.getNext();
				MovieDBItem item=new MovieDBItem(tempItemNode.getItem().getGenre(), tempItemNode.getItem().getTitle());
				results.add(item);
			}
		}
    	return results;
    }
}

class Genre extends Node<String> implements Comparable<Genre> {
	public Genre(String name) {
		super(name);
		throw new UnsupportedOperationException("not implemented yet");
	}

	@Override
	public int compareTo(Genre o) {
		throw new UnsupportedOperationException("not implemented yet");
	}

	@Override
	public int hashCode() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	@Override
	public boolean equals(Object obj) {
		throw new UnsupportedOperationException("not implemented yet");
	}
}

class MovieList implements ListInterface<String> {
	public MovieList() {
	}

	@Override
	public Iterator<String> iterator() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	@Override
	public boolean isEmpty() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	@Override
	public int size() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	@Override
	public void add(String item) {
		throw new UnsupportedOperationException("not implemented yet");
	}

	@Override
	public String first() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	@Override
	public void removeAll() {
		throw new UnsupportedOperationException("not implemented yet");
	}
}