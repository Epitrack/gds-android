package com.epitrack.guardioes.view.menu.profile;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.model.SingleUser;
import com.epitrack.guardioes.model.User;
import com.epitrack.guardioes.request.Method;
import com.epitrack.guardioes.request.Requester;
import com.epitrack.guardioes.request.SimpleRequester;
import com.epitrack.guardioes.utility.DateFormat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author Igor Morais
 */
public class UserAdapter extends ArrayAdapter<User> {

    private final UserListener listener;
    SingleUser singleUser = SingleUser.getInstance();
    private ArrayList<User> userArrayList;

    public UserAdapter(final Context context, final ArrayList<User> userList, final UserListener listener) {
        super(context, 0, userList);

        this.userArrayList = userList;
        this.listener = listener;
    }

    class ViewHolder {

        TextView textViewName;
        TextView textViewType;
        ImageView imageViewImage;
        ImageView imageTrash;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup viewGroup) {
        View view = convertView;
        User user = null;
        ViewHolder viewHolder = null;
        try {

            if (view == null) {

                LayoutInflater inflater = (LayoutInflater) viewGroup.getContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


                view = inflater.inflate(R.layout.user_item, viewGroup, false);

                viewHolder = new ViewHolder();

                viewHolder.textViewName = (TextView) view.findViewById(R.id.text_view_name);
                viewHolder.textViewType = (TextView) view.findViewById(R.id.text_view_type);
                viewHolder.imageViewImage = (ImageView) view.findViewById(R.id.image_view_image);

                view.findViewById(R.id.linear_layout).setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(final View itemView) {
                        listener.onEdit(getItem(position));
                    }
                });

                view.findViewById(R.id.image_view_trash).setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(final View itemView) {
                        listener.onDelete(getItem(position));
                    }
                });

                view.setTag(viewHolder);

            } else {

                viewHolder = (ViewHolder) view.getTag();
            }

            user = userArrayList.get(position); //getItem(position);

            if (user.getId() == singleUser.getId()) {
                viewHolder.imageTrash = (ImageView) view.findViewById(R.id.image_view_trash);
                viewHolder.imageTrash.setVisibility(View.INVISIBLE);
            }

            viewHolder.textViewName.setText(user.getNick());
            viewHolder.textViewType.setText(user.getType());

            if (user.getPicture() == null) {
                user.setPicture("0");
            }

            if (user.getPicture().equals("")) {
                user.setPicture("0");
            }

            if (user.getPicture().length() > 2) {
                try {
                    String url = user.getPicture();

                    if (url.substring(0, 4).toLowerCase().equals("http")) {
                        user.setPicture("0");

                        int age = DateFormat.getDateDiff(singleUser.getDob());

                        if (user.getGender().equals("F")) {
                            if (user.getRace().equals("preto") || user.getRace().equals("indigena") || user.getRace().equals("pardo")) {
                                if(age > 49) {
                                    viewHolder.imageViewImage.setImageResource(R.drawable.avatar_3);
                                } else if(age > 25) {
                                    viewHolder.imageViewImage.setImageResource(R.drawable.avatar_2);
                                } else {
                                    viewHolder.imageViewImage.setImageResource(R.drawable.avatar_1);
                                }
                            }
                            else if(user.getRace().equals("amarelo"))
                            {
                                if(age > 49) {
                                    viewHolder.imageViewImage.setImageResource(R.drawable.avatar_9);
                                } else if(age > 25) {
                                    viewHolder.imageViewImage.setImageResource(R.drawable.avatar_8);
                                } else {
                                    viewHolder.imageViewImage.setImageResource(R.drawable.avatar_7);
                                }
                            }
                            else if(user.getRace().equals("branco"))
                            {
                                if(age > 49) {
                                    viewHolder.imageViewImage.setImageResource(R.drawable.avatar_14);
                                } else if(age > 25) {
                                    viewHolder.imageViewImage.setImageResource(R.drawable.avatar_8);
                                } else {
                                    viewHolder.imageViewImage.setImageResource(R.drawable.avatar_13);
                                }
                            }
                        } else if (user.getGender().equals("M")) {
                            if (user.getRace().equals("preto") || singleUser.getRace().equals("indigena") || singleUser.getRace().equals("pardo")) {
                                if(age > 49) {
                                    viewHolder.imageViewImage.setImageResource(R.drawable.avatar_6);
                                } else if(age > 25) {
                                    viewHolder.imageViewImage.setImageResource(R.drawable.avatar_5);
                                } else {
                                    viewHolder.imageViewImage.setImageResource(R.drawable.avatar_4);
                                }
                            }
                            else if(user.getRace().equals("amarelo"))
                            {
                                if(age > 49) {
                                    viewHolder.imageViewImage.setImageResource(R.drawable.avatar_12);
                                } else if(age > 25) {
                                    viewHolder.imageViewImage.setImageResource(R.drawable.avatar_11);
                                } else {
                                    viewHolder.imageViewImage.setImageResource(R.drawable.avatar_10);
                                }
                            } else if(user.getRace().equals("branco")) {
                                if(age > 49) {
                                    viewHolder.imageViewImage.setImageResource(R.drawable.avatar_16);
                                } else if(age > 25) {
                                    viewHolder.imageViewImage.setImageResource(R.drawable.avatar_11);
                                } else {
                                    viewHolder.imageViewImage.setImageResource(R.drawable.avatar_15);
                                }
                            }
                        }
                    } else {
                        Uri uri = Uri.parse(user.getPicture());
                        File file = new File(user.getPicture());

                        if (!file.exists()) {
                            viewHolder.imageViewImage.setImageURI(uri);
                            Drawable drawable = viewHolder.imageViewImage.getDrawable();
                            viewHolder.imageViewImage.setImageDrawable(drawable);

                            if (drawable == null) {
                                //aqui
                                int age = DateFormat.getDateDiff(singleUser.getDob());

                                if (user.getGender().equals("F")) {
                                    if (user.getRace().equals("preto") || user.getRace().equals("indigena") || user.getRace().equals("pardo")) {
                                        if(age > 49) {
                                            viewHolder.imageViewImage.setImageResource(R.drawable.avatar_3);
                                        } else if(age > 25) {
                                            viewHolder.imageViewImage.setImageResource(R.drawable.avatar_2);
                                        } else {
                                            viewHolder.imageViewImage.setImageResource(R.drawable.avatar_1);
                                        }
                                    }
                                    else if(user.getRace().equals("amarelo"))
                                    {
                                        if(age > 49) {
                                            viewHolder.imageViewImage.setImageResource(R.drawable.avatar_9);
                                        } else if(age > 25) {
                                            viewHolder.imageViewImage.setImageResource(R.drawable.avatar_8);
                                        } else {
                                            viewHolder.imageViewImage.setImageResource(R.drawable.avatar_7);
                                        }
                                    }
                                    else if(user.getRace().equals("branco"))
                                    {
                                        if(age > 49) {
                                            viewHolder.imageViewImage.setImageResource(R.drawable.avatar_14);
                                        } else if(age > 25) {
                                            viewHolder.imageViewImage.setImageResource(R.drawable.avatar_8);
                                        } else {
                                            viewHolder.imageViewImage.setImageResource(R.drawable.avatar_13);
                                        }
                                    }
                                } else if (user.getGender().equals("M")) {
                                    if (user.getRace().equals("preto") || singleUser.getRace().equals("indigena") || singleUser.getRace().equals("pardo")) {
                                        if(age > 49) {
                                            viewHolder.imageViewImage.setImageResource(R.drawable.avatar_6);
                                        } else if(age > 25) {
                                            viewHolder.imageViewImage.setImageResource(R.drawable.avatar_5);
                                        } else {
                                            viewHolder.imageViewImage.setImageResource(R.drawable.avatar_4);
                                        }
                                    }
                                    else if(user.getRace().equals("amarelo"))
                                    {
                                        if(age > 49) {
                                            viewHolder.imageViewImage.setImageResource(R.drawable.avatar_12);
                                        } else if(age > 25) {
                                            viewHolder.imageViewImage.setImageResource(R.drawable.avatar_11);
                                        } else {
                                            viewHolder.imageViewImage.setImageResource(R.drawable.avatar_10);
                                        }
                                    } else if(user.getRace().equals("branco")) {
                                        if(age > 49) {
                                            viewHolder.imageViewImage.setImageResource(R.drawable.avatar_16);
                                        } else if(age > 25) {
                                            viewHolder.imageViewImage.setImageResource(R.drawable.avatar_11);
                                        } else {
                                            viewHolder.imageViewImage.setImageResource(R.drawable.avatar_15);
                                        }
                                    }
                                }
                            }
                        } else {
                            viewHolder.imageViewImage.setImageURI(uri);
                        }
                    }
                } catch (Exception e) {
                    user.setPicture("0");
                    //aqui
                    int age = DateFormat.getDateDiff(singleUser.getDob());

                    if (user.getGender().equals("F")) {
                        if (user.getRace().equals("preto") || user.getRace().equals("indigena") || user.getRace().equals("pardo")) {
                            if(age > 49) {
                                viewHolder.imageViewImage.setImageResource(R.drawable.avatar_3);
                            } else if(age > 25) {
                                viewHolder.imageViewImage.setImageResource(R.drawable.avatar_2);
                            } else {
                                viewHolder.imageViewImage.setImageResource(R.drawable.avatar_1);
                            }
                        }
                        else if(user.getRace().equals("amarelo"))
                        {
                            if(age > 49) {
                                viewHolder.imageViewImage.setImageResource(R.drawable.avatar_9);
                            } else if(age > 25) {
                                viewHolder.imageViewImage.setImageResource(R.drawable.avatar_8);
                            } else {
                                viewHolder.imageViewImage.setImageResource(R.drawable.avatar_7);
                            }
                        }
                        else if(user.getRace().equals("branco"))
                        {
                            if(age > 49) {
                                viewHolder.imageViewImage.setImageResource(R.drawable.avatar_14);
                            } else if(age > 25) {
                                viewHolder.imageViewImage.setImageResource(R.drawable.avatar_8);
                            } else {
                                viewHolder.imageViewImage.setImageResource(R.drawable.avatar_13);
                            }
                        }
                    } else if (user.getGender().equals("M")) {
                        if (user.getRace().equals("preto") || singleUser.getRace().equals("indigena") || singleUser.getRace().equals("pardo")) {
                            if(age > 49) {
                                viewHolder.imageViewImage.setImageResource(R.drawable.avatar_6);
                            } else if(age > 25) {
                                viewHolder.imageViewImage.setImageResource(R.drawable.avatar_5);
                            } else {
                                viewHolder.imageViewImage.setImageResource(R.drawable.avatar_4);
                            }
                        }
                        else if(user.getRace().equals("amarelo"))
                        {
                            if(age > 49) {
                                viewHolder.imageViewImage.setImageResource(R.drawable.avatar_12);
                            } else if(age > 25) {
                                viewHolder.imageViewImage.setImageResource(R.drawable.avatar_11);
                            } else {
                                viewHolder.imageViewImage.setImageResource(R.drawable.avatar_10);
                            }
                        } else if(user.getRace().equals("branco")) {
                            if(age > 49) {
                                viewHolder.imageViewImage.setImageResource(R.drawable.avatar_16);
                            } else if(age > 25) {
                                viewHolder.imageViewImage.setImageResource(R.drawable.avatar_11);
                            } else {
                                viewHolder.imageViewImage.setImageResource(R.drawable.avatar_15);
                            }
                        }
                    }

                }
            } else {
                user.setPicture(user.getPicture());

                if (Integer.parseInt(user.getPicture()) == 0) {
                    //aqqui
                    int age = DateFormat.getDateDiff(singleUser.getDob());

                    if (user.getGender().equals("F")) {
                        if (user.getRace().equals("preto") || user.getRace().equals("indigena") || user.getRace().equals("pardo")) {
                            if(age > 49) {
                                viewHolder.imageViewImage.setImageResource(R.drawable.avatar_3);
                            } else if(age > 25) {
                                viewHolder.imageViewImage.setImageResource(R.drawable.avatar_2);
                            } else {
                                viewHolder.imageViewImage.setImageResource(R.drawable.avatar_1);
                            }
                        }
                        else if(user.getRace().equals("amarelo"))
                        {
                            if(age > 49) {
                                viewHolder.imageViewImage.setImageResource(R.drawable.avatar_9);
                            } else if(age > 25) {
                                viewHolder.imageViewImage.setImageResource(R.drawable.avatar_8);
                            } else {
                                viewHolder.imageViewImage.setImageResource(R.drawable.avatar_7);
                            }
                        }
                        else if(user.getRace().equals("branco"))
                        {
                            if(age > 49) {
                                viewHolder.imageViewImage.setImageResource(R.drawable.avatar_14);
                            } else if(age > 25) {
                                viewHolder.imageViewImage.setImageResource(R.drawable.avatar_8);
                            } else {
                                viewHolder.imageViewImage.setImageResource(R.drawable.avatar_13);
                            }
                        }
                    } else if (user.getGender().equals("M")) {
                        if (user.getRace().equals("preto") || singleUser.getRace().equals("indigena") || singleUser.getRace().equals("pardo")) {
                            if(age > 49) {
                                viewHolder.imageViewImage.setImageResource(R.drawable.avatar_6);
                            } else if(age > 25) {
                                viewHolder.imageViewImage.setImageResource(R.drawable.avatar_5);
                            } else {
                                viewHolder.imageViewImage.setImageResource(R.drawable.avatar_4);
                            }
                        }
                        else if(user.getRace().equals("amarelo"))
                        {
                            if(age > 49) {
                                viewHolder.imageViewImage.setImageResource(R.drawable.avatar_12);
                            } else if(age > 25) {
                                viewHolder.imageViewImage.setImageResource(R.drawable.avatar_11);
                            } else {
                                viewHolder.imageViewImage.setImageResource(R.drawable.avatar_10);
                            }
                        } else if(user.getRace().equals("branco")) {
                            if(age > 49) {
                                viewHolder.imageViewImage.setImageResource(R.drawable.avatar_16);
                            } else if(age > 25) {
                                viewHolder.imageViewImage.setImageResource(R.drawable.avatar_11);
                            } else {
                                viewHolder.imageViewImage.setImageResource(R.drawable.avatar_15);
                            }
                        }
                    }

                } else {
                    viewHolder.imageViewImage.setImageResource(Avatar.getBy(Integer.parseInt(user.getPicture())).getSmall());
                }
            }
        } catch (Exception e) {
            //aqui
            int age = DateFormat.getDateDiff(singleUser.getDob());

            if (user.getGender().equals("F")) {
                if (user.getRace().equals("preto") || user.getRace().equals("indigena") || user.getRace().equals("pardo")) {
                    if(age > 49) {
                        viewHolder.imageViewImage.setImageResource(R.drawable.avatar_3);
                    } else if(age > 25) {
                        viewHolder.imageViewImage.setImageResource(R.drawable.avatar_2);
                    } else {
                        viewHolder.imageViewImage.setImageResource(R.drawable.avatar_1);
                    }
                }
                else if(user.getRace().equals("amarelo"))
                {
                    if(age > 49) {
                        viewHolder.imageViewImage.setImageResource(R.drawable.avatar_9);
                    } else if(age > 25) {
                        viewHolder.imageViewImage.setImageResource(R.drawable.avatar_8);
                    } else {
                        viewHolder.imageViewImage.setImageResource(R.drawable.avatar_7);
                    }
                }
                else if(user.getRace().equals("branco"))
                {
                    if(age > 49) {
                        viewHolder.imageViewImage.setImageResource(R.drawable.avatar_14);
                    } else if(age > 25) {
                        viewHolder.imageViewImage.setImageResource(R.drawable.avatar_8);
                    } else {
                        viewHolder.imageViewImage.setImageResource(R.drawable.avatar_13);
                    }
                }
            } else if (user.getGender().equals("M")) {
                if (user.getRace().equals("preto") || singleUser.getRace().equals("indigena") || singleUser.getRace().equals("pardo")) {
                    if(age > 49) {
                        viewHolder.imageViewImage.setImageResource(R.drawable.avatar_6);
                    } else if(age > 25) {
                        viewHolder.imageViewImage.setImageResource(R.drawable.avatar_5);
                    } else {
                        viewHolder.imageViewImage.setImageResource(R.drawable.avatar_4);
                    }
                }
                else if(user.getRace().equals("amarelo"))
                {
                    if(age > 49) {
                        viewHolder.imageViewImage.setImageResource(R.drawable.avatar_12);
                    } else if(age > 25) {
                        viewHolder.imageViewImage.setImageResource(R.drawable.avatar_11);
                    } else {
                        viewHolder.imageViewImage.setImageResource(R.drawable.avatar_10);
                    }
                } else if(user.getRace().equals("branco")) {
                    if(age > 49) {
                        viewHolder.imageViewImage.setImageResource(R.drawable.avatar_16);
                    } else if(age > 25) {
                        viewHolder.imageViewImage.setImageResource(R.drawable.avatar_11);
                    } else {
                        viewHolder.imageViewImage.setImageResource(R.drawable.avatar_15);
                    }
                }
            }

        } finally {
            return view;
        }

    }
}